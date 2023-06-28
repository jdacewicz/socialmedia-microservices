package pl.jdacewicz.postservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.jdacewicz.postservice.dto.CommentDto;
import pl.jdacewicz.postservice.dto.mapper.CommentMapper;
import pl.jdacewicz.postservice.model.Comment;
import pl.jdacewicz.postservice.service.CommentService;

@RestController
@Transactional
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
    public CommentDto getVisibleCommentById(@PathVariable long id) {
        Comment comment = commentService.getVisibleCommentById(id);
        return commentMapper.convertToDto(comment);
    }

    @GetMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<CommentDto> getPostComments(@PathVariable long postId,
                                            @RequestParam(defaultValue = "true") boolean visible,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size,
                                            @RequestParam(defaultValue = "creationTime") String sort,
                                            @RequestParam(defaultValue = "ASC") String directory) {
        return commentService.getPostComments(postId, visible, page, size, sort, directory)
                .map(commentMapper::convertToDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void changePostVisibility(@PathVariable long id,
                                     @RequestParam boolean visible) {
        commentService.changeCommentVisibility(id, visible);
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
