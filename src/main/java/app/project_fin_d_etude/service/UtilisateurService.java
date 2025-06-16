package app.project_fin_d_etude.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import app.project_fin_d_etude.model.Utilisateur;
import app.project_fin_d_etude.repository.UtilisateurRepository;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Async
    public CompletableFuture<Optional<Utilisateur>> findByEmail(String email) {
        return CompletableFuture.completedFuture(utilisateurRepository.findByEmail(email));
    }

    @Async
    public CompletableFuture<Optional<Utilisateur>> findById(Long id) {
        return CompletableFuture.completedFuture(utilisateurRepository.findById(id));
    }

    @Async
    public CompletableFuture<Boolean> existsByEmail(String email) {
        return CompletableFuture.completedFuture(utilisateurRepository.existsByEmail(email));
    }

    @Async
    public CompletableFuture<Utilisateur> save(Utilisateur utilisateur) {
        return CompletableFuture.completedFuture(utilisateurRepository.save(utilisateur));
    }

    @Async
    public CompletableFuture<Void> delete(Utilisateur utilisateur) {
        utilisateurRepository.delete(utilisateur);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<List<Utilisateur>> findAll() {
        return CompletableFuture.completedFuture(utilisateurRepository.findAll());
    }
}
