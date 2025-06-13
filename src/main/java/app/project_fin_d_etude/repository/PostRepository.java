package app.project_fin_d_etude.repository;

import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.model.CategoriePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByCategorie(CategoriePost categorie);

    List<Post> findByTitreContainingIgnoreCase(String keyword);
}
