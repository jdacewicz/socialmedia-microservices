package pl.jdacewicz.sharingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.jdacewicz.sharingservice.dto.CommentRequest;
import pl.jdacewicz.sharingservice.dto.PostDto;
import pl.jdacewicz.sharingservice.dto.PostRequest;
import pl.jdacewicz.sharingservice.dto.mapper.CommentMapper;
import pl.jdacewicz.sharingservice.dto.mapper.PostMapper;
import pl.jdacewicz.sharingservice.model.Comment;
import pl.jdacewicz.sharingservice.model.Post;
import pl.jdacewicz.sharingservice.service.PostService;
import pl.jdacewicz.sharingservice.util.ApiVersion;

@RestController
@Transactional
@RequestMapping(value = "${spring.application.api-url}" + "/posts",
        headers = "X-API-VERSION=1",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public PostController(PostService postService, PostMapper postMapper, CommentMapper commentMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public PostDto getPostById(@PathVariable long id) {
        Post post = postService.getVisiblePostById(id);
        return postMapper.convertToDto(post);
    }

    @PostMapping
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@AuthenticationPrincipal Jwt jwt,
                              @RequestBody PostRequest postRequest) {
        Post post = postMapper.convertFromRequest(postRequest);
        String userEmail = jwt.getClaim("email");
        Post createdPost = postService.createPost(userEmail, post);
        return postMapper.convertToDto(createdPost);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.OK)
    public void changePostVisibility(@PathVariable long id,
                                     @RequestParam boolean visible) {
        postService.changePostVisibility(id, visible);
    }

    @PutMapping("/{postId}/react/{reactionId}")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public void reactToPost(@PathVariable long postId,
                            @PathVariable int reactionId) {
        postService.reactToPost(postId, reactionId);
    }

    @PutMapping("/{postId}/comment")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public void commentPost(@AuthenticationPrincipal Jwt jwt,
                            @PathVariable long postId,
                            @RequestBody CommentRequest commentRequest) {
        Comment comment = commentMapper.convertFromRequest(commentRequest);
        String userEmail = jwt.getClaim("email");
        postService.commentPost(userEmail, postId, comment);
    }

    @PutMapping("/{postId}/groups/{groupId}/add")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public void addPostToGroup(@PathVariable long postId,
                               @PathVariable long groupId) {
        postService.addPostToGroup(postId, groupId);
    }
    @PutMapping("/{postId}/groups/{groupId}/remove")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public void removePostFromGroup(@PathVariable long postId,
                               @PathVariable long groupId) {
        postService.removePostFromGroup(postId, groupId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable long id) {
        postService.deletePost(id);
    }
}
