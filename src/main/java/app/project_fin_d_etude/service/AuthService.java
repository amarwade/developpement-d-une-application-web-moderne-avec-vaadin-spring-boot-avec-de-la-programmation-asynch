package app.project_fin_d_etude.service;

import app.project_fin_d_etude.model.Utilisateur;
import app.project_fin_d_etude.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public AuthService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Transactional
    public void creerUtilisateur(String email, String nom) {
        // Vérifier si l'email existe déjà
        if (utilisateurRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        // Créer le nouvel utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email.toLowerCase().trim());
        utilisateur.setNom(nom.trim());
        utilisateur.setDateCreation(LocalDateTime.now());
        utilisateur.setActif(true);
        utilisateur.setRole(Utilisateur.Role.UTILISATEUR);

        try {
            utilisateurRepository.save(utilisateur);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du compte : " + e.getMessage());
        }
    }

    public Optional<Utilisateur> getUtilisateurByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    @Transactional
    public void updateProfile(String email, String nom) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (nom != null && !nom.trim().isEmpty()) {
            utilisateur.setNom(nom.trim());
            utilisateurRepository.save(utilisateur);
        } else {
            throw new RuntimeException("Le nom ne peut pas être vide");
        }
    }

    @Transactional
    public void desactiverCompte(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        utilisateur.setActif(false);
        utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public void reactiverCompte(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        utilisateur.setActif(true);
        utilisateurRepository.save(utilisateur);
    }
}
