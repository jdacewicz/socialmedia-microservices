package pl.jdacewicz.postservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jdacewicz.postservice.dto.PostRequest;
import pl.jdacewicz.postservice.entity.Post;
import pl.jdacewicz.postservice.repository.PostRepository;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public void createPost(PostRequest postRequest) {
        Post post = Post.builder()
                .content(postRequest.content())
                .build();
        postRepository.save(post);
    }
}
