package pl.jdacewicz.postservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.jdacewicz.postservice.dto.PostRequest;
import pl.jdacewicz.postservice.entity.Post;
import pl.jdacewicz.postservice.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping(headers = "Accept=application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPost(@RequestBody PostRequest postRequest) {
        postService.createPost(postRequest);
    }
}
