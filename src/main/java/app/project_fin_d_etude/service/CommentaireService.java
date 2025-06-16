package app.project_fin_d_etude.service;

import app.project_fin_d_etude.model.Commentaire;
import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.repository.CommentaireRepository;
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
        return CompletableFuture.completedFuture(commentaireRepository.findByPost(post));
    }

    @Async
    public CompletableFuture<Commentaire> save(Commentaire commentaire) {
        return CompletableFuture.completedFuture(commentaireRepository.save(commentaire));
    }

    @Async
    public CompletableFuture<Void> delete(Long id) {
        commentaireRepository.deleteById(id);
        return CompletableFuture.completedFuture(null);
    }
}
