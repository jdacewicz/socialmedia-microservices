package pl.jdacewicz.postservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jdacewicz.postservice.exception.RecordNotFoundException;
import pl.jdacewicz.postservice.model.Post;
import pl.jdacewicz.postservice.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post getVisiblePostById(long id) {
        return postRepository.findByIdAndVisibleIs(id, true)
                .orElseThrow(() -> new RecordNotFoundException("Post with given id doesn't exist"));
    }

    public Post changePostVisibility(long id, boolean visible) {
        return postRepository.findById(id)
                .map(post -> Post.builder()
                        .id(post.getId())
                        .creationTime(post.getCreationTime())
                        .content(post.getContent())
                        .visible(visible)
                        .build())
                .orElseThrow(() -> new RecordNotFoundException("Post with given id doesn't exist"));
    }
}
