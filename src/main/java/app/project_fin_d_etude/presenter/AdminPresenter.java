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
                utilisateurService.findAll()
                        .thenAccept(utilisateurs -> view.afficherUtilisateurs(utilisateurs))
                        .exceptionally(e -> {
                            view.afficherErreur("Erreur lors du chargement des utilisateurs : " + e.getMessage());
                            return null;
                        });
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors du chargement des utilisateurs : " + e.getMessage());
            }
        }
    }

    public void supprimerUtilisateur(Long id) {
        try {
            utilisateurService.findById(id)
                    .thenAccept(utilisateurOpt -> {
                        if (utilisateurOpt.isPresent()) {
                            utilisateurService.delete(utilisateurOpt.get())
                                    .thenRun(() -> {
                                        if (view != null) {
                                            view.afficherMessage("Utilisateur supprimé avec succès");
                                            chargerUtilisateurs();
                                        }
                                    });
                        } else {
                            view.afficherErreur("Utilisateur non trouvé");
                        }
                    })
                    .exceptionally(e -> {
                        view.afficherErreur("Erreur lors de la suppression : " + e.getMessage());
                        return null;
                    });
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la suppression : " + e.getMessage());
            }
        }
    }

    public void modifierUtilisateur(Utilisateur utilisateur) {
        try {
            utilisateurService.save(utilisateur)
                    .thenAccept(savedUtilisateur -> {
                        if (view != null) {
                            view.afficherMessage("Utilisateur modifié avec succès");
                            chargerUtilisateurs();
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
}
