package app.project_fin_d_etude.service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import app.project_fin_d_etude.model.Profile;
import app.project_fin_d_etude.model.Utilisateur;
import app.project_fin_d_etude.repository.ProfileRepository;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Async
    public CompletableFuture<Optional<Profile>> getProfileByUtilisateur(Utilisateur utilisateur) {
        return CompletableFuture.completedFuture(profileRepository.findByUtilisateur(utilisateur));
    }

    @Async
    public CompletableFuture<Profile> save(Profile profile) {
        return CompletableFuture.completedFuture(profileRepository.save(profile));
    }

    @Async
    public CompletableFuture<java.util.List<Profile>> getAllProfiles() {
        return CompletableFuture.completedFuture(profileRepository.findAll());
    }
}
