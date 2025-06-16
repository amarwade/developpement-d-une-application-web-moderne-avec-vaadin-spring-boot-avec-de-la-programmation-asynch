package app.project_fin_d_etude.presenter;

import java.util.List;
import org.springframework.stereotype.Component;
import app.project_fin_d_etude.model.CategoriePost;
import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.service.PostService;
import lombok.Setter;

/**
 * Presenter pour la gestion des articles (posts). Cette classe fait le lien
 * entre les vues et le service de gestion des articles. Elle implémente le
 * pattern MVP (Model-View-Presenter).
 */
@Component
public class PostPresenter {

    @Setter
    private PostView view;

    private final PostService postService;

    /**
     * Interface définissant les méthodes de callback pour la vue. Ces méthodes
     * sont appelées par le presenter pour mettre à jour l'interface
     * utilisateur.
     */
    public interface PostView {

        void afficherCategories(List<CategoriePost> categories);

        void afficherPosts(List<Post> posts);

        void afficherMessage(String message);

        void afficherErreur(String erreur);

        void viderFormulaire();

        void redirigerVersDetail(Long postId);

        void mettreAJourPagination(int totalItems);

        void afficherPost(Post post);
    }

    /**
     * Constructeur du presenter.
     *
     * @param postService Service de gestion des articles
     */
    public PostPresenter(PostService postService) {
        this.postService = postService;
    }

    /**
     * Charge la liste des catégories disponibles. Les catégories sont
     * récupérées depuis l'énumération CategoriePost.
     */
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

    /**
     * Charge la liste de tous les articles de manière asynchrone. Utilise
     * CompletableFuture pour gérer l'asynchronicité.
     */
    public void chargerPosts() {
        try {
            if (view != null) {
                postService.getAllPosts()
                        .thenAccept(posts -> view.afficherPosts(posts))
                        .exceptionally(e -> {
                            view.afficherErreur("Erreur lors du chargement des articles : " + e.getMessage());
                            return null;
                        });
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors du chargement des articles : " + e.getMessage());
            }
        }
    }

    /**
     * Publie un nouvel article. Vérifie la validité des données avant la
     * publication.
     *
     * @param post L'article à publier
     */
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

            postService.save(post)
                    .thenAccept(savedPost -> {
                        if (view != null) {
                            view.afficherMessage("Article publié avec succès !");
                            view.viderFormulaire();
                            view.redirigerVersDetail(savedPost.getId());
                        }
                    })
                    .exceptionally(e -> {
                        view.afficherErreur("Erreur lors de la publication : " + e.getMessage());
                        return null;
                    });
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la publication : " + e.getMessage());
            }
        }
    }

    /**
     * Modifie un article existant. Vérifie la validité des données avant la
     * modification.
     *
     * @param post L'article à modifier
     */
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

            postService.save(post)
                    .thenAccept(updatedPost -> {
                        if (view != null) {
                            view.afficherMessage("Article modifié avec succès !");
                            view.redirigerVersDetail(updatedPost.getId());
                        }
                    })
                    .exceptionally(e -> {
                        view.afficherErreur("Erreur lors de la modification : " + e.getMessage());
                        return null;
                    });
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la modification : " + e.getMessage());
            }
        }
    }

    /**
     * Supprime un article. Vérifie d'abord l'existence de l'article avant la
     * suppression.
     *
     * @param postId L'identifiant de l'article à supprimer
     */
    public void supprimer(Long postId) {
        try {
            postService.getPostById(postId)
                    .thenAccept(post -> {
                        if (post.isPresent()) {
                            postService.delete(postId)
                                    .thenRun(() -> {
                                        if (view != null) {
                                            view.afficherMessage("Article supprimé avec succès");
                                            chargerPosts();
                                        }
                                    })
                                    .exceptionally(e -> {
                                        view.afficherErreur("Erreur lors de la suppression : " + e.getMessage());
                                        return null;
                                    });
                        } else {
                            view.afficherErreur("Article non trouvé");
                        }
                    });
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la suppression : " + e.getMessage());
            }
        }
    }

    /**
     * Charge un article spécifique par son identifiant.
     *
     * @param postId L'identifiant de l'article à charger
     */
    public void chargerPost(Long postId) {
        try {
            postService.getPostById(postId)
                    .thenAccept(post -> {
                        if (post.isPresent()) {
                            if (view != null) {
                                view.afficherPost(post.get());
                                view.mettreAJourPagination(post.get().getCommentaires().size());
                            }
                        } else {
                            if (view != null) {
                                view.afficherErreur("Article non trouvé");
                            }
                        }
                    })
                    .exceptionally(e -> {
                        if (view != null) {
                            view.afficherErreur("Erreur lors du chargement de l'article : " + e.getMessage());
                        }
                        return null;
                    });
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors du chargement de l'article : " + e.getMessage());
            }
        }
    }

    /**
     * Recherche des articles par mot-clé.
     *
     * @param keyword Le mot-clé de recherche
     */
    public void rechercherArticles(String keyword) {
        try {
            postService.searchPosts(keyword)
                    .thenAccept(posts -> {
                        if (view != null) {
                            view.afficherPosts(posts);
                        }
                    })
                    .exceptionally(e -> {
                        view.afficherErreur("Erreur lors de la recherche : " + e.getMessage());
                        return null;
                    });
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la recherche : " + e.getMessage());
            }
        }
    }

    /**
     * Charge une page d'articles avec pagination.
     *
     * @param page Le numéro de la page
     * @param pageSize Le nombre d'articles par page
     */
    public void loadPaginatedPosts(int page, int pageSize) {
        try {
            if (view != null) {
                postService.getPaginatedPosts(page, pageSize)
                        .thenAccept(posts -> view.afficherPosts(posts))
                        .exceptionally(e -> {
                            view.afficherErreur("Erreur lors du chargement des articles paginés : " + e.getMessage());
                            return null;
                        });
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors du chargement des articles paginés : " + e.getMessage());
            }
        }
    }
}
