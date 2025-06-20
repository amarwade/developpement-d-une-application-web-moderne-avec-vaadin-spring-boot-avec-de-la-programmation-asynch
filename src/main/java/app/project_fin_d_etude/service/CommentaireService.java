package app.project_fin_d_etude.service;

import app.project_fin_d_etude.model.Commentaire;
import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.repository.CommentaireRepository;
import app.project_fin_d_etude.utils.EntityValidator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CommentaireService {

    private final CommentaireRepository commentaireRepository;

    public CommentaireService(CommentaireRepository commentaireRepository) {
        this.commentaireRepository = commentaireRepository;
    }

    @Async
    public CompletableFuture<List<Commentaire>> getCommentairesByPost(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Le post ne peut pas être null");
        }
        return CompletableFuture.completedFuture(commentaireRepository.findByPost(post));
    }

    @Async
    public CompletableFuture<Commentaire> save(Commentaire commentaire) {
        // Validation de l'entité avant sauvegarde
        EntityValidator.ValidationResult validationResult = EntityValidator.validateCommentaire(commentaire);
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException("Commentaire invalide: " + validationResult.getAllErrorsAsString());
        }

        return CompletableFuture.completedFuture(commentaireRepository.save(commentaire));
    }

    @Async
    public CompletableFuture<Void> delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID du commentaire ne peut pas être null");
        }
        commentaireRepository.deleteById(id);
        return CompletableFuture.completedFuture(null);
    }
}
