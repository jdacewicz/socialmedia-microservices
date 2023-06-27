package pl.jdacewicz.postservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<Comment> getPostComments(long postId, int page, int size, String sort, boolean desc) {
        Sort pageSort = (desc) ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        Pageable paging = PageRequest.of(page, size, pageSort);

        return commentRepository.findAllByPostId(postId, paging);
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
