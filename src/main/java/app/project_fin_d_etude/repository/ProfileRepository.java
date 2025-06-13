package app.project_fin_d_etude.repository;


import app.project_fin_d_etude.model.Profile;
import app.project_fin_d_etude.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUtilisateur(Utilisateur utilisateur);
}

