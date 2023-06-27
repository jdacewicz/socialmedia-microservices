package pl.jdacewicz.postservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.jdacewicz.postservice.dto.CommentDto;
import pl.jdacewicz.postservice.dto.mapper.CommentMapper;
import pl.jdacewicz.postservice.model.Comment;
import pl.jdacewicz.postservice.service.CommentService;

@RestController
@RequestMapping(value = "/api/comments", headers = "Accept=application/json")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getCommentById(@PathVariable long id) {
        Comment comment = commentService.getCommentById(id);
        return commentMapper.convertToDto(comment);
    }

    @PutMapping("/{commentId}/react/{reactionId}")
    @ResponseStatus(HttpStatus.OK)
    public void reactToComment(@PathVariable long commentId,
                               @PathVariable int reactionId) {
        commentService.reactToComment(commentId, reactionId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable long id) {
        commentService.deleteComment(id);
    }
}
