package app.project_fin_d_etude.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import app.project_fin_d_etude.model.CategoriePost;
import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> searchPosts(String keyword) {
        return postRepository.findByTitreContainingIgnoreCase(keyword);
    }

    public List<Post> getByCategorie(CategoriePost categorie) {
        return postRepository.findByCategorie(categorie);
    }

    public List<Post> getPaginatedPosts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return postRepository.findAll(pageable).getContent();
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    @Async
    public CompletableFuture<List<Post>> getAllPostsAsync() {
        return CompletableFuture.completedFuture(postRepository.findAll());
    }
}
