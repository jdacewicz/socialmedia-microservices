package pl.jdacewicz.sharingservice.service;

import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.*;
import pl.jdacewicz.sharingservice.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final ReactionService reactionService;
    private final PostGroupService postGroupService;
    private final UserService userService;

    @Autowired
    public PostService(PostRepository postRepository, ReactionService reactionService,
                       PostGroupService postGroupService, UserService userService) {
        this.postRepository = postRepository;
        this.reactionService = reactionService;
        this.postGroupService = postGroupService;
        this.userService = userService;
    }

    public Post getVisiblePostById(long id) {
        return postRepository.findByIdAndVisibleIs(id, true)
                .orElseThrow(() -> new RecordNotFoundException("Could not find post with id: " + id));
    }

    public Post createPost(String userEmail, Post post) {
        User user = userService.getUserByEmail(userEmail);
        post.setCreator(user);
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

    public void commentPost(String userEmail, long postId, Comment comment) {
        User user = userService.getUserByEmail(userEmail);
        comment.setCreator(user);
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

    public void removePostFromGroup(long postId, long groupId) {
        PostGroup group = postGroupService.getPostGroupById(groupId);

        postRepository.findById(postId)
                .map(post -> {
                    post.removePostGroup(group);
                    return postRepository.save(post);
                }).orElseThrow(() -> new RecordNotFoundException("Could not find group with id: " + groupId));
    }

    public void deletePost(long id) {
        postRepository.deleteById(id);
    }
}
