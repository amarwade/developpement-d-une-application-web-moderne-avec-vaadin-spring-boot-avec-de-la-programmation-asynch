package app.project_fin_d_etude.service;

import app.project_fin_d_etude.model.Commentaire;
import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.repository.CommentaireRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentaireService {

    private final CommentaireRepository commentaireRepository;

    public CommentaireService(CommentaireRepository commentaireRepository) {
        this.commentaireRepository = commentaireRepository;
    }

    public List<Commentaire> getCommentairesByPost(Post post) {
        return commentaireRepository.findByPost(post);
    }

    public Commentaire save(Commentaire commentaire) {
        return commentaireRepository.save(commentaire);
    }

    public void delete(Long id) {
        commentaireRepository.deleteById(id);
    }
}

