package app.project_fin_d_etude.service;

import java.util.Optional;

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

    public Optional<Profile> getProfileByUtilisateur(Utilisateur utilisateur) {
        return profileRepository.findByUtilisateur(utilisateur);
    }

    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }
}
