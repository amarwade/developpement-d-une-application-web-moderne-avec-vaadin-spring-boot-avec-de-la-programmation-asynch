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

    @Async
    public CompletableFuture<List<Post>> getAllPosts() {
        return CompletableFuture.completedFuture(postRepository.findAll());
    }

    @Async
    public CompletableFuture<Optional<Post>> getPostById(Long id) {
        return CompletableFuture.completedFuture(postRepository.findById(id));
    }

    @Async
    public CompletableFuture<List<Post>> searchPosts(String keyword) {
        Pageable pageable = PageRequest.of(0, 10); // Page 0 avec 10 résultats
        return CompletableFuture.completedFuture(postRepository.searchPosts(keyword, pageable).getContent());
    }

    @Async
    public CompletableFuture<List<Post>> getByCategorie(CategoriePost categorie) {
        Pageable pageable = PageRequest.of(0, 10);
        return CompletableFuture.completedFuture(postRepository.findByCategorie(categorie, pageable).getContent());
    }

    @Async
    public CompletableFuture<List<Post>> getPaginatedPosts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return CompletableFuture.completedFuture(postRepository.findAll(pageable).getContent());
    }

    @Async
    public CompletableFuture<Post> save(Post post) {
        return CompletableFuture.completedFuture(postRepository.save(post));
    }

    @Async
    public CompletableFuture<Void> delete(Long id) {
        postRepository.deleteById(id);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<List<Post>> getAllPostsAsync() {
        return CompletableFuture.completedFuture(postRepository.findAll());
    }
}
