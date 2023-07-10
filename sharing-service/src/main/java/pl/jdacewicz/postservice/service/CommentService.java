package pl.jdacewicz.postservice.service;

import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.jdacewicz.postservice.exception.RecordNotFoundException;
import pl.jdacewicz.postservice.model.Comment;
import pl.jdacewicz.postservice.model.Reaction;
import pl.jdacewicz.postservice.repository.CommentRepository;
import pl.jdacewicz.postservice.util.PageableUtils;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReactionService reactionService;

    @Autowired
    public CommentService(CommentRepository commentRepository, ReactionService reactionService) {
        this.commentRepository = commentRepository;
        this.reactionService = reactionService;
    }

    public Comment getVisibleCommentById(long id) {
        return commentRepository.findByIdAndVisible(id, true)
                .orElseThrow(() -> new RecordNotFoundException("Could not find comment with id: " + id));
    }

    public Page<Comment> getPostComments(long postId, boolean visible, int page, int size, String sort, String directory) {
        Pageable paging = PageableUtils.createPageable(page, size, sort, directory);
        return commentRepository.findAllByPostIdAndVisible(postId, visible, paging);
    }

    @Transient
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

    public void deleteComment(long id) {
        commentRepository.deleteById(id);
    }
}
