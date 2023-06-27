package pl.jdacewicz.postservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jdacewicz.postservice.exception.RecordNotFoundException;
import pl.jdacewicz.postservice.model.Comment;
import pl.jdacewicz.postservice.model.Reaction;
import pl.jdacewicz.postservice.repository.CommentRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReactionService reactionService;

    @Autowired
    public CommentService(CommentRepository commentRepository, ReactionService reactionService) {
        this.commentRepository = commentRepository;
        this.reactionService = reactionService;
    }


    public Comment getCommentById(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Could not find comment with id: " + id));
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
