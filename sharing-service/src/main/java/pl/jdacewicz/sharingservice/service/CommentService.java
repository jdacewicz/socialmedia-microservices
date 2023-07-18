package pl.jdacewicz.sharingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.*;
import pl.jdacewicz.sharingservice.repository.CommentRepository;
import pl.jdacewicz.sharingservice.util.FileUtils;
import pl.jdacewicz.sharingservice.util.PageableUtils;

import java.io.IOException;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReactionService reactionService;
    private final UserService userService;
    private final PostService postService;
    private final AdvertisementService advertisementService;

    @Autowired
    public CommentService(CommentRepository commentRepository, ReactionService reactionService,
                          UserService userService, PostService postService,
                          AdvertisementService advertisementService) {
        this.commentRepository = commentRepository;
        this.reactionService = reactionService;
        this.userService = userService;
        this.postService = postService;
        this.advertisementService = advertisementService;
    }

    public Comment getCommentById(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Could not find comment with id: " + id));
    }

    public Comment getVisibleCommentById(long id) {
        return commentRepository.findByIdAndVisible(id, true)
                .orElseThrow(() -> new RecordNotFoundException("Could not find comment with id: " + id));
    }

    public Page<Comment> getPostComments(long postId, boolean visible, int page, int size, String sort, String directory) {
        Pageable paging = PageableUtils.createPageable(page, size, sort, directory);
        return commentRepository.findAllByPostIdAndVisible(postId, visible, paging);
    }

    public Comment commentPost(String userEmail, long postId, String content, MultipartFile image) throws IOException {
        User user = userService.getUserByEmail(userEmail);
        Post post = postService.getVisiblePostById(postId);
        String newFileName = FileUtils.generateFileName(image.getOriginalFilename());

        Comment comment = Comment.builder()
                .content(content)
                .image(newFileName)
                .creator(user)
                .post(post)
                .build();
        post.addComment(comment);
        Comment createdComment = commentRepository.save(comment);

        FileUtils.saveFile(image, newFileName, createdComment.getDirectoryPath());
        return createdComment;
    }

    public Comment commentAdvertisement(String userEmail, int advertisementId, String content, MultipartFile image)
            throws IOException {
        User user = userService.getUserByEmail(userEmail);
        Advertisement advertisement = advertisementService.getActiveAdvertisementById(advertisementId);
        String newFileName = FileUtils.generateFileName(image.getOriginalFilename());

        Comment comment = Comment.builder()
                .content(content)
                .image(newFileName)
                .creator(user)
                .advertisement(advertisement)
                .build();
        advertisement.addComment(comment);
        Comment createdComment = commentRepository.save(comment);

        FileUtils.saveFile(image, newFileName, comment.getDirectoryPath());
        return createdComment;
    }

    @Transactional
    public void changeCommentVisibility(long id, boolean visible) {
        commentRepository.setVisibilityBy(id, visible);
    }

    public void reactToComment(long commentId, int reactionId) {
        Reaction reaction = reactionService.getReactionById(reactionId);

        commentRepository.findById(commentId)
                .map(comment -> {
                    comment.addReaction(reaction);
                    return commentRepository.save(comment);
                }).orElseThrow(() -> new RecordNotFoundException("Could not find comment with id: " + commentId));
    }

    public void deleteComment(long id) throws IOException {
        Comment comment = getCommentById(id);

        FileUtils.deleteFile(comment.getDirectoryPath(), comment.getImage());
        commentRepository.deleteById(id);
    }
}
