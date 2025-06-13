package app.project_fin_d_etude.presenter;

import java.util.List;

import org.springframework.stereotype.Component;

import app.project_fin_d_etude.model.Utilisateur;
import app.project_fin_d_etude.service.UtilisateurService;
import lombok.Setter;

@Component
public class AdminPresenter {

    private final UtilisateurService utilisateurService;

    @Setter
    private AdminView view;

    public interface AdminView {

        void afficherUtilisateurs(List<Utilisateur> utilisateurs);

        void afficherMessage(String message);

        void afficherErreur(String erreur);
    }

    public AdminPresenter(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    public void chargerUtilisateurs() {
        try {
            if (view != null) {
                List<Utilisateur> utilisateurs = utilisateurService.findAll();
                view.afficherUtilisateurs(utilisateurs);
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors du chargement des utilisateurs : " + e.getMessage());
            }
        }
    }

    public void supprimerUtilisateur(Long id) {
        try {
            Utilisateur utilisateur = utilisateurService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            utilisateurService.delete(utilisateur);
            if (view != null) {
                view.afficherMessage("Utilisateur supprimé avec succès");
                chargerUtilisateurs(); // Recharger la liste
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la suppression : " + e.getMessage());
            }
        }
    }

    public void modifierUtilisateur(Utilisateur utilisateur) {
        try {
            utilisateurService.save(utilisateur);
            if (view != null) {
                view.afficherMessage("Utilisateur modifié avec succès");
                chargerUtilisateurs(); // Recharger la liste
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la modification : " + e.getMessage());
            }
        }
    }
}
