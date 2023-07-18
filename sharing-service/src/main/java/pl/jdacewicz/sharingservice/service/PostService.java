package pl.jdacewicz.sharingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.Post;
import pl.jdacewicz.sharingservice.model.PostGroup;
import pl.jdacewicz.sharingservice.model.Reaction;
import pl.jdacewicz.sharingservice.model.User;
import pl.jdacewicz.sharingservice.repository.PostRepository;
import pl.jdacewicz.sharingservice.util.FileUtils;

import java.io.IOException;

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

    public Post createPost(String userEmail, String content, MultipartFile image) throws IOException {
        User user = userService.getUserByEmail(userEmail);
        String newFileName = FileUtils.generateFileName(image.getOriginalFilename());

        Post post = Post.builder()
                .creator(user)
                .content(content)
                .image(newFileName)
                .build();
        Post createdPost = postRepository.save(post);

        FileUtils.saveFile(image, newFileName, createdPost.getDirectoryPath());
        return createdPost;
    }

    @Transactional
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
