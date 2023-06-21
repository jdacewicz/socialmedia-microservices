package pl.jdacewicz.postservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.jdacewicz.postservice.dto.PostDto;
import pl.jdacewicz.postservice.dto.PostRequest;
import pl.jdacewicz.postservice.dto.mapper.PostMapper;
import pl.jdacewicz.postservice.model.Post;
import pl.jdacewicz.postservice.service.PostService;

@RestController
@Transactional
@RequestMapping(value = "/api/posts", headers = "Accept=application/json")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    @Autowired
    public PostController(PostService postService, PostMapper postMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto getPostById(@PathVariable long id) {
        Post post = postService.getVisiblePostById(id);
        return postMapper.convertToDto(post);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@RequestBody PostRequest postRequest) {
        Post post = postMapper.convertFromRequest(postRequest);
        Post createdPost = postService.createPost(post);
        return postMapper.convertToDto(createdPost);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void changePostVisibility(@PathVariable long id,
                                     @RequestParam boolean visible) {
        postService.changePostVisibility(id, visible);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable long id) {
        postService.deletePost(id);
    }
}
