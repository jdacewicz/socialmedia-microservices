package pl.jdacewicz.postservice.service;

import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jdacewicz.postservice.exception.RecordNotFoundException;
import pl.jdacewicz.postservice.model.Comment;
import pl.jdacewicz.postservice.model.Post;
import pl.jdacewicz.postservice.model.PostGroup;
import pl.jdacewicz.postservice.model.Reaction;
import pl.jdacewicz.postservice.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final ReactionService reactionService;
    private final PostGroupService postGroupService;

    @Autowired
    public PostService(PostRepository postRepository, ReactionService reactionService, PostGroupService postGroupService) {
        this.postRepository = postRepository;
        this.reactionService = reactionService;
        this.postGroupService = postGroupService;
    }

    public Post getVisiblePostById(long id) {
        return postRepository.findByIdAndVisibleIs(id, true)
                .orElseThrow(() -> new RecordNotFoundException("Could not find post with id: " + id));
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Transient
    public void changePostVisibility(long id, boolean visible) {
        postRepository.setVisibleById(id, visible);
    }

    public void reactToPost(long postId, int reactionId) {
        Reaction reaction = reactionService.getReactionById(reactionId);

       postRepository.findById(postId)
               .map(post -> {
                    post.addReaction(reaction);
                    return postRepository.save(post);
        }).orElseThrow(() -> new RecordNotFoundException("Could not find post with id: " + postId));
    }

    public void commentPost(long postId, Comment comment) {
        postRepository.findById(postId)
                .map(post -> {
                    post.addComment(comment);
                    return postRepository.save(post);
        }).orElseThrow(() -> new RecordNotFoundException("Could not find post with id: " + postId));
    }

    public void addPostToGroup(long postId, long groupId) {
        PostGroup group = postGroupService.getPostGroupById(groupId);

        postRepository.findById(postId)
                .map(post -> {
                    post.addPostGroup(group);
                    return postRepository.save(post);
                }).orElseThrow(() -> new RecordNotFoundException("Could not find group with id: " + groupId));
    }

    public void deletePost(long id) {
        postRepository.deleteById(id);
    }
}
