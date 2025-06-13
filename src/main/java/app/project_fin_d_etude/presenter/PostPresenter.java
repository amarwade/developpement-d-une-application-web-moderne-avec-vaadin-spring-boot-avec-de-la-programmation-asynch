package app.project_fin_d_etude.presenter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import app.project_fin_d_etude.model.CategoriePost;
import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.service.PostService;
import lombok.Setter;

@Component
public class PostPresenter {

    @Setter
    private PostView view;

    private final PostService postService;

    public interface PostView {

        void afficherCategories(List<CategoriePost> categories);

        void afficherPosts(List<Post> posts);

        void afficherMessage(String message);

        void afficherErreur(String erreur);

        void viderFormulaire();

        void redirigerVersDetail(Long postId);
    }

    public PostPresenter(PostService postService) {
        this.postService = postService;
    }

    public void chargerCategories() {
        try {
            if (view != null) {
                List<CategoriePost> categories = List.of(CategoriePost.values());
                view.afficherCategories(categories);
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors du chargement des catégories : " + e.getMessage());
            }
        }
    }

    public void chargerPosts() {
        try {
            if (view != null) {
                List<Post> posts = postService.getAllPosts();
                view.afficherPosts(posts);
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors du chargement des articles : " + e.getMessage());
            }
        }
    }

    public void publier(Post post) {
        try {
            if (post.getTitre() == null || post.getTitre().trim().isEmpty()) {
                view.afficherErreur("Le titre de l'article ne peut pas être vide");
                return;
            }
            if (post.getContenu() == null || post.getContenu().trim().isEmpty()) {
                view.afficherErreur("Le contenu de l'article ne peut pas être vide");
                return;
            }
            if (post.getCategorie() == null) {
                view.afficherErreur("Veuillez sélectionner une catégorie");
                return;
            }

            Post savedPost = postService.save(post);
            if (view != null) {
                view.afficherMessage("Article publié avec succès !");
                view.viderFormulaire();
                view.redirigerVersDetail(savedPost.getId());
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la publication : " + e.getMessage());
            }
        }
    }

    public void modifier(Post post) {
        try {
            if (post.getTitre() == null || post.getTitre().trim().isEmpty()) {
                view.afficherErreur("Le titre de l'article ne peut pas être vide");
                return;
            }
            if (post.getContenu() == null || post.getContenu().trim().isEmpty()) {
                view.afficherErreur("Le contenu de l'article ne peut pas être vide");
                return;
            }
            if (post.getCategorie() == null) {
                view.afficherErreur("Veuillez sélectionner une catégorie");
                return;
            }

            Post updatedPost = postService.save(post);
            if (view != null) {
                view.afficherMessage("Article modifié avec succès !");
                view.redirigerVersDetail(updatedPost.getId());
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la modification : " + e.getMessage());
            }
        }
    }

    public void supprimer(Long postId) {
        try {
            Optional<Post> post = postService.getPostById(postId);
            if (post.isPresent()) {
                postService.delete(postId);
                if (view != null) {
                    view.afficherMessage("Article supprimé avec succès");
                    chargerPosts(); // Rafraîchir la liste
                }
            } else {
                view.afficherErreur("Article non trouvé");
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la suppression : " + e.getMessage());
            }
        }
    }

    public void chargerPost(Long postId) {
        try {
            Optional<Post> post = postService.getPostById(postId);
            if (post.isPresent()) {
                // Implémenter la logique pour afficher le post
                // Cette méthode dépendra de la structure de votre vue
            } else {
                view.afficherErreur("Article non trouvé");
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors du chargement de l'article : " + e.getMessage());
            }
        }
    }

    public List<Post> getAllPosts() {
        try {
            return postService.getAllPosts();
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors du chargement des articles : " + e.getMessage());
            }
            return List.of();
        }
    }

    public List<Post> rechercherArticles(String keyword) {
        try {
            List<Post> posts = postService.searchPosts(keyword);
            if (view != null) {
                view.afficherPosts(posts);
            }
            return posts;
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la recherche : " + e.getMessage());
            }
            return List.of();
        }
    }

    public void loadPaginatedPosts(int page, int pageSize) {
        try {
            if (view != null) {
                List<Post> posts = postService.getPaginatedPosts(page, pageSize);
                view.afficherPosts(posts);
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors du chargement des articles paginés : " + e.getMessage());
            }
        }
    }
}
