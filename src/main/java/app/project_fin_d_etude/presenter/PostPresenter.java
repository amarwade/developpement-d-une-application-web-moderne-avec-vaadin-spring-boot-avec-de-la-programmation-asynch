package app.project_fin_d_etude.presenter;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.service.PostService;

@Component
public class PostPresenter {

    private PostView view;
    private final PostService postService;
    private static final int ITEMS_PER_PAGE = 6;

    @Autowired
    public PostPresenter(PostService postService) {
        this.postService = postService;
    }

    public void setView(PostView view) {
        this.view = view;
    }

    public interface PostView {

        void afficherPost(Post post);

        void afficherPosts(List<Post> posts);

        void afficherMessage(String message);

        void afficherErreur(String erreur);

        void viderFormulaire();

        void redirigerVersDetail(Long postId);

        void mettreAJourPagination(int totalItems);
    }

    public void chargerPosts() {
        loadPaginatedPosts(0, ITEMS_PER_PAGE);
    }

    public void chargerPost(Long postId) {
        handleAsyncOperation(
                postService.getPostById(postId),
                "Erreur lors du chargement de l'article",
                optionalPost -> {
                    if (optionalPost.isPresent()) {
                        Post post = optionalPost.get();
                        view.afficherPost(post);
                        if (post.getCommentaires() != null) {
                            view.mettreAJourPagination(post.getCommentaires().size());
                        }
                    } else {
                        view.afficherErreur("Article non trouvé");
                    }
                }
        );
    }

    public void publierPost(Post post) {
        if (validatePost(post)) {
            handleAsyncOperation(
                    postService.savePost(post),
                    "Erreur lors de la publication",
                    savedPost -> {
                        view.afficherMessage("Article publié avec succès !");
                        view.viderFormulaire();
                        view.redirigerVersDetail(savedPost.getId());
                    }
            );
        }
    }

    public void modifierPost(Post post) {
        if (validatePost(post)) {
            handleAsyncOperation(
                    postService.savePost(post),
                    "Erreur lors de la modification",
                    updatedPost -> {
                        view.afficherMessage("Article modifié avec succès !");
                        view.redirigerVersDetail(updatedPost.getId());
                    }
            );
        }
    }

    public void supprimerPost(Long postId) {
        handleAsyncOperation(
                postService.delete(postId),
                "Erreur lors de la suppression",
                unused -> {
                    view.afficherMessage("Article supprimé avec succès");
                    chargerPosts();
                }
        );
    }

    public void rechercherArticles(String keyword, int page, int size) {
        handleAsyncOperation(
                postService.searchPosts(keyword, page, size),
                "Erreur lors de la recherche",
                pageResult -> {
                    view.afficherPosts(pageResult.getContent());
                    view.mettreAJourPagination((int) pageResult.getTotalElements());
                }
        );
    }

    public void loadPaginatedPosts(int page, int size) {
        handleAsyncOperation(
                postService.getPaginatedPosts(page, size),
                "Erreur lors du chargement des articles paginés",
                view::afficherPosts
        );
    }

    private boolean validatePost(Post post) {
        if (post.getTitre() == null || post.getTitre().trim().isEmpty()) {
            view.afficherErreur("Le titre de l'article ne peut pas être vide");
            return false;
        }
        if (post.getContenu() == null || post.getContenu().trim().isEmpty()) {
            view.afficherErreur("Le contenu de l'article ne peut pas être vide");
            return false;
        }
        return true;
    }

    private <T> void handleAsyncOperation(
            CompletableFuture<T> future,
            String errorMessage,
            java.util.function.Consumer<T> successHandler
    ) {
        if (view == null) {
            return;
        }

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                view.afficherErreur(errorMessage + ": " + ex.getMessage());
            } else {
                try {
                    successHandler.accept(result);
                } catch (Exception e) {
                    view.afficherErreur("Erreur lors du traitement des données: " + e.getMessage());
                }
            }
        });
    }
}
