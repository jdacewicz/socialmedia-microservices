package pl.jdacewicz.sharingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.jdacewicz.sharingservice.dto.CommentDto;
import pl.jdacewicz.sharingservice.dto.mapper.CommentMapper;
import pl.jdacewicz.sharingservice.model.Comment;
import pl.jdacewicz.sharingservice.service.CommentService;

@RestController
@Transactional
@RequestMapping(value = "${spring.application.api-url}" + "/comments",
        headers = "X-API-VERSION=1",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getVisibleCommentById(@PathVariable long id) {
        Comment comment = commentService.getVisibleCommentById(id);
        return commentMapper.convertToDto(comment);
    }

    @GetMapping("/post/{postId}")
    @PreAuthorize("hasRole('user')")
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
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.OK)
    public void changePostVisibility(@PathVariable long id,
                                     @RequestParam boolean visible) {
        commentService.changeCommentVisibility(id, visible);
    }

    @PutMapping("/{commentId}/react/{reactionId}")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public void reactToComment(@PathVariable long commentId,
                               @PathVariable int reactionId) {
        commentService.reactToComment(commentId, reactionId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable long id) {
        commentService.deleteComment(id);
    }
}
