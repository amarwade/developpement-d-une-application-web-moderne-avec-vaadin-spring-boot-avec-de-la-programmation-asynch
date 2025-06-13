package app.project_fin_d_etude.repository;

import app.project_fin_d_etude.model.Commentaire;
import app.project_fin_d_etude.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {
    List<Commentaire> findByPost(Post post);
}

