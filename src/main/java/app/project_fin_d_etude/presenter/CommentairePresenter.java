package app.project_fin_d_etude.presenter;

import java.util.List;

import org.springframework.stereotype.Component;

import app.project_fin_d_etude.model.Commentaire;
import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.service.CommentaireService;
import lombok.Setter;

@Component
public class CommentairePresenter {

    @Setter
    private CommentaireView view;

    private final CommentaireService commentaireService;

    public interface CommentaireView {

        void afficherCommentaires(List<Commentaire> commentaires);

        void afficherMessage(String message);

        void afficherErreur(String erreur);

        void rafraichirListe();
    }

    public CommentairePresenter(CommentaireService commentaireService) {
        this.commentaireService = commentaireService;
    }

    public void chargerCommentaires(Post post) {
        try {
            if (view != null) {
                List<Commentaire> commentaires = commentaireService.getCommentairesByPost(post);
                view.afficherCommentaires(commentaires);
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors du chargement des commentaires : " + e.getMessage());
            }
        }
    }

    public void ajouter(Commentaire commentaire) {
        try {
            if (commentaire.getContenu() == null || commentaire.getContenu().trim().isEmpty()) {
                view.afficherErreur("Le contenu du commentaire ne peut pas être vide");
                return;
            }

            commentaireService.save(commentaire);
            if (view != null) {
                view.afficherMessage("Commentaire ajouté avec succès");
                view.rafraichirListe();
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de l'ajout du commentaire : " + e.getMessage());
            }
        }
    }

    public void supprimer(Commentaire commentaire) {
        try {
            commentaireService.delete(commentaire.getId());
            if (view != null) {
                view.afficherMessage("Commentaire supprimé avec succès");
                view.rafraichirListe();
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la suppression du commentaire : " + e.getMessage());
            }
        }
    }

    public void modifier(Commentaire commentaire) {
        try {
            if (commentaire.getContenu() == null || commentaire.getContenu().trim().isEmpty()) {
                view.afficherErreur("Le contenu du commentaire ne peut pas être vide");
                return;
            }

            commentaireService.save(commentaire);
            if (view != null) {
                view.afficherMessage("Commentaire modifié avec succès");
                view.rafraichirListe();
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la modification du commentaire : " + e.getMessage());
            }
        }
    }
}
