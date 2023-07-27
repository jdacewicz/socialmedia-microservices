package pl.jdacewicz.sharingservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.dto.PostDto;
import pl.jdacewicz.sharingservice.dto.PostRequest;
import pl.jdacewicz.sharingservice.dto.mapper.PostMapper;
import pl.jdacewicz.sharingservice.model.Post;
import pl.jdacewicz.sharingservice.service.PostService;
import pl.jdacewicz.sharingservice.validation.ValidFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "${spring.application.api-url}" + "/posts",
        headers = "X-API-VERSION=1",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    @Autowired
    public PostController(PostService postService, PostMapper postMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('user')")
    public PostDto getPostById(@PathVariable long id) {
        Post post = postService.getVisiblePostById(id);
        return postMapper.convertToDto(post);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@AuthenticationPrincipal Jwt jwt,
                              @Valid @RequestPart PostRequest request,
                              @ValidFile @RequestPart MultipartFile image) throws IOException {
        String userEmail = jwt.getClaim("email");
        Post createdPost = postService.createPost(userEmail, request, image);
        return postMapper.convertToDto(createdPost);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public void changePostVisibility(@PathVariable long id,
                                     @RequestParam boolean visible) {
        postService.changePostVisibility(id, visible);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public void deletePost(@PathVariable long id) throws IOException {
        postService.deletePost(id);
    }
}
