package app.project_fin_d_etude.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.project_fin_d_etude.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE LOWER(p.titre) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "OR LOWER(p.contenu) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Post> searchPosts(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p ORDER BY p.datePublication DESC")
    Page<Post> findAllOrderByDatePublicationDesc(Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.commentaires LEFT JOIN FETCH p.auteur WHERE p.id = :id")
    Optional<Post> findByIdWithCommentsAndAuthor(@Param("id") Long id);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.auteur")
    List<Post> findAllWithAuteur();

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.auteur ORDER BY p.datePublication DESC")
    List<Post> findAllWithAuteurPaged(Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.auteur WHERE p.id IN :ids ORDER BY p.datePublication DESC")
    List<Post> findAllWithAuteurByIds(@Param("ids") List<Long> ids);

    default CompletableFuture<List<Post>> getAllPosts() {
        return CompletableFuture.supplyAsync(() -> {
            List<Post> posts = findAllWithAuteur();
            // Forcer le chargement de l'auteur pour chaque post
            for (Post post : posts) {
                if (post.getAuteur() != null) {
                    post.getAuteur().getNom();
                }
            }
            return posts;
        });
    }
}
