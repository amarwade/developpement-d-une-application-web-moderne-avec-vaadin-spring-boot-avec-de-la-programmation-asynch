package app.project_fin_d_etude.repository;

import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.model.CategoriePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.categorie = :categorie ORDER BY p.datePublication DESC")
    Page<Post> findByCategorie(@Param("categorie") CategoriePost categorie, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE LOWER(p.titre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.contenu) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Post> searchPosts(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p ORDER BY p.datePublication DESC")
    Page<Post> findAllOrderByDatePublicationDesc(Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.commentaires WHERE p.id = :id")
    Optional<Post> findByIdWithComments(@Param("id") Long id);
}
