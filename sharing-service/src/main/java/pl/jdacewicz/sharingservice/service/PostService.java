package pl.jdacewicz.sharingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.dto.PostRequest;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.Post;
import pl.jdacewicz.sharingservice.model.User;
import pl.jdacewicz.sharingservice.repository.PostRepository;
import pl.jdacewicz.sharingservice.util.FileUtils;

import java.io.IOException;

@Service
public class PostService {

    @Value("${message.post.not-found}")
    private String notFoundMessage;

    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public Post getPostById(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(notFoundMessage));
    }

    public Post getVisiblePostById(long id) {
        return postRepository.findByIdAndVisibleIs(id, true)
                .orElseThrow(() -> new RecordNotFoundException(notFoundMessage));
    }

    public Post createPost(String userEmail, PostRequest request, MultipartFile image) throws IOException {
        User user = userService.getUserByEmail(userEmail);
        String newFileName = FileUtils.generateFileName(image.getOriginalFilename());

        Post post = Post.builder()
                .creator(user)
                .content(request.content())
                .image(newFileName)
                .build();
        Post createdPost = postRepository.save(post);

        FileUtils.saveFile(image, newFileName, createdPost.getDirectoryPath());
        return createdPost;
    }

    @Transactional
    public void changePostVisibility(long id, boolean visible) {
        postRepository.setVisibleById(id, visible);
    }

    public void deletePost(long id) throws IOException {
        Post post = getPostById(id);

        FileUtils.deleteDirectory(post.getDirectoryPath());
        postRepository.deleteById(id);
    }
}
