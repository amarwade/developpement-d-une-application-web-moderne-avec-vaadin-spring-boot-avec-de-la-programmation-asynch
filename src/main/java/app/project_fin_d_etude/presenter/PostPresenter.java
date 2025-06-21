package app.project_fin_d_etude.presenter;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.service.PostService;
import com.vaadin.flow.component.UI;

/**
 * Présentateur pour la gestion des articles (posts) avec pattern MVP.
 */
@Component
public class PostPresenter {

    private PostView view;
    private final PostService postService;
    private static final int ITEMS_PER_PAGE = 6;

    @Autowired
    public PostPresenter(PostService postService) {
        this.postService = postService;
    }

    /**
     * Associe une vue à ce présentateur.
     */
    public void setView(PostView view) {
        this.view = view;
    }

    /**
     * Interface à implémenter par la vue pour lier le présentateur.
     */
    public interface PostView {

        void afficherPost(Post post);

        void afficherPosts(List<Post> posts);

        void afficherMessage(String message);

        void afficherErreur(String erreur);

        void viderFormulaire();

        void redirigerVersDetail(Long postId);

        void mettreAJourPagination(int totalItems);
    }

    /**
     * Charge la première page d'articles.
     */
    public void chargerPosts() {
        if (view == null) {
            return;
        }
        loadPaginatedPosts(0, ITEMS_PER_PAGE);
    }

    /**
     * Charge un article par son identifiant.
     */
    public void chargerPost(Long postId) {
        if (view == null) {
            return;
        }
        PostView currentView = this.view;
        handleAsyncOperation(
                postService.getPostById(postId),
                "Erreur lors du chargement de l'article",
                optionalPost -> {
                    if (optionalPost.isPresent()) {
                        Post post = optionalPost.get();
                        currentView.afficherPost(post);
                        if (post.getCommentaires() != null) {
                            currentView.mettreAJourPagination(post.getCommentaires().size());
                        }
                    } else {
                        currentView.afficherErreur("Article non trouvé");
                    }
                }
        );
    }

    /**
     * Publie un nouvel article après validation.
     */
    public void publierPost(Post post) {
        if (view == null) {
            return;
        }
        PostView currentView = this.view;
        if (validatePost(post, currentView)) {
            handleAsyncOperation(
                    postService.savePost(post),
                    "Erreur lors de la publication",
                    savedPost -> {
                        currentView.afficherMessage("Article publié avec succès !");
                        currentView.viderFormulaire();
                        currentView.redirigerVersDetail(savedPost.getId());
                    }
            );
        }
    }

    /**
     * Modifie un article existant après validation.
     */
    public void modifierPost(Post post) {
        if (view == null) {
            return;
        }
        PostView currentView = this.view;
        if (validatePost(post, currentView)) {
            handleAsyncOperation(
                    postService.savePost(post),
                    "Erreur lors de la modification",
                    updatedPost -> {
                        currentView.afficherMessage("Article modifié avec succès !");
                        currentView.redirigerVersDetail(updatedPost.getId());
                    }
            );
        }
    }

    /**
     * Supprime un article par son identifiant.
     */
    public void supprimerPost(Long postId) {
        if (view == null) {
            return;
        }
        PostView currentView = this.view;
        handleAsyncOperation(
                postService.delete(postId),
                "Erreur lors de la suppression",
                unused -> {
                    currentView.afficherMessage("Article supprimé avec succès");
                    chargerPosts();
                }
        );
    }

    /**
     * Recherche des articles par mot-clé avec pagination.
     */
    public void rechercherArticles(String keyword, int page, int size) {
        if (view == null) {
            return;
        }
        PostView currentView = this.view;
        handleAsyncOperation(
                postService.searchPosts(keyword, page, size),
                "Erreur lors de la recherche",
                pageResult -> {
                    currentView.afficherPosts(pageResult.getContent());
                    currentView.mettreAJourPagination((int) pageResult.getTotalElements());
                }
        );
    }

    /**
     * Charge une page d'articles avec pagination.
     */
    public void loadPaginatedPosts(int page, int size) {
        if (view == null) {
            return;
        }
        PostView currentView = this.view;
        handleAsyncOperation(
                postService.getPaginatedPosts(page, size),
                "Erreur lors du chargement des articles paginés",
                currentView::afficherPosts
        );
    }

    /**
     * Valide les champs obligatoires d'un article.
     */
    private boolean validatePost(Post post, PostView currentView) {
        if (post.getTitre() == null || post.getTitre().trim().isEmpty()) {
            currentView.afficherErreur("Le titre de l'article ne peut pas être vide");
            return false;
        }
        if (post.getContenu() == null || post.getContenu().trim().isEmpty()) {
            currentView.afficherErreur("Le contenu de l'article ne peut pas être vide");
            return false;
        }
        return true;
    }

    /**
     * Gère les opérations asynchrones et la gestion des erreurs.
     */
    private <T> void handleAsyncOperation(
            CompletableFuture<T> future,
            String errorMessage,
            java.util.function.Consumer<T> successHandler
    ) {
        PostView currentView = this.view;
        if (currentView == null) {
            return;
        }

        UI ui = UI.getCurrent();
        if (ui == null) {
            // Log or handle the case where UI is not available
            return;
        }

        future.whenComplete((result, ex) -> {
            ui.access(() -> {
                if (ex != null) {
                    currentView.afficherErreur(errorMessage + ": " + ex.getCause().getMessage());
                } else {
                    try {
                        successHandler.accept(result);
                    } catch (Exception e) {
                        currentView.afficherErreur("Erreur lors du traitement des données: " + e.getMessage());
                    }
                }
            });
        });
    }
}
