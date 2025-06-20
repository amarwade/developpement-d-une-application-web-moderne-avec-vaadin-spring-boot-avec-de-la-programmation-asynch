package app.project_fin_d_etude.presenter;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import app.project_fin_d_etude.model.Profile;
import app.project_fin_d_etude.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminPresenter {

    private AdminView view;
    private final ProfileService profileService;

    @Autowired
    public AdminPresenter(ProfileService profileService) {
        this.profileService = profileService;
    }

    public void setView(AdminView view) {
        this.view = view;
    }

    public void chargerProfiles() {
        profileService.getAllProfiles().thenAccept(profiles -> {
            if (view != null) {
                view.afficherProfiles(profiles);
            }
        }).exceptionally(ex -> {
            if (view != null) {
                view.afficherErreur("Erreur lors du chargement des profils : " + ex.getMessage());
            }
            return null;
        });
    }

    public interface AdminView {

        void afficherProfiles(List<Profile> profiles);

        void afficherMessage(String message);

        void afficherErreur(String erreur);
    }
}
