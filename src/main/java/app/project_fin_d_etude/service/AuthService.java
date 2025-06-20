package app.project_fin_d_etude.service;

import app.project_fin_d_etude.model.Utilisateur;
import app.project_fin_d_etude.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Async
    @Transactional
    public CompletableFuture<Void> creerUtilisateur(String email, String nom, String motDePasse) {
        if (utilisateurRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email.toLowerCase().trim());
        utilisateur.setNom(nom.trim());
        utilisateur.setMotDePasse(passwordEncoder.encode(motDePasse));
        utilisateur.setDateCreation(LocalDateTime.now());
        utilisateur.setActif(true);
        utilisateur.setRole(Utilisateur.Role.UTILISATEUR);

        try {
            utilisateurRepository.save(utilisateur);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du compte : " + e.getMessage());
        }
    }

    @Async
    public CompletableFuture<Optional<Utilisateur>> getUtilisateurByEmail(String email) {
        return CompletableFuture.completedFuture(utilisateurRepository.findByEmail(email));
    }

    @Async
    @Transactional
    public CompletableFuture<Void> updateProfile(String email, String nom) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (nom != null && !nom.trim().isEmpty()) {
            utilisateur.setNom(nom.trim());
            utilisateurRepository.save(utilisateur);
            return CompletableFuture.completedFuture(null);
        } else {
            throw new RuntimeException("Le nom ne peut pas être vide");
        }
    }

    @Async
    @Transactional
    public CompletableFuture<Void> changerMotDePasse(String email, String ancienMotDePasse, String nouveauMotDePasse) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(ancienMotDePasse, utilisateur.getMotDePasse())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }

        utilisateur.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
        utilisateurRepository.save(utilisateur);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Transactional
    public CompletableFuture<Void> desactiverCompte(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        utilisateur.setActif(false);
        utilisateurRepository.save(utilisateur);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Transactional
    public CompletableFuture<Void> reactiverCompte(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        utilisateur.setActif(true);
        utilisateurRepository.save(utilisateur);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> updateDerniereConnexion(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        utilisateur.setDerniereConnexion(LocalDateTime.now());
        utilisateurRepository.save(utilisateur);
        return CompletableFuture.completedFuture(null);
    }
}
