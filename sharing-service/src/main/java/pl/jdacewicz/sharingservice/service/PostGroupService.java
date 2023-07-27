package pl.jdacewicz.sharingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.dto.PostGroupRequest;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.Post;
import pl.jdacewicz.sharingservice.model.PostGroup;
import pl.jdacewicz.sharingservice.model.User;
import pl.jdacewicz.sharingservice.repository.PostGroupRepository;
import pl.jdacewicz.sharingservice.util.FileUtils;

import java.io.IOException;

@Service
public class PostGroupService {

    @Value("${message.post-group.not-found}")
    private String notFoundMessage;

    private final PostGroupRepository postGroupRepository;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public PostGroupService(PostGroupRepository postGroupRepository, UserService userService, PostService postService) {
        this.postGroupRepository = postGroupRepository;
        this.userService = userService;
        this.postService = postService;
    }

    public PostGroup getPostGroupById(long id) {
        return postGroupRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(notFoundMessage));
    }

    public PostGroup createGroup(String userEmail, PostGroupRequest request, MultipartFile image) throws IOException {
        User user = userService.getUserByEmail(userEmail);
        String newFileName = FileUtils.generateFileName(image.getOriginalFilename());

        PostGroup postGroup = PostGroup.builder()
                .name(request.name())
                .image(newFileName)
                .creator(user)
                .build();
        PostGroup createdGroup = postGroupRepository.save(postGroup);

        FileUtils.saveFile(image, newFileName, createdGroup.getDirectoryPath());
        return createdGroup;
    }

    public PostGroup updateGroup(long id, PostGroupRequest request, MultipartFile image) throws IOException {
        PostGroup postGroup = getPostGroupById(id);

        postGroup.setName(request.name());
        FileUtils.saveFile(image, postGroup.getImage(), postGroup.getDirectoryPath());
        return postGroupRepository.save(postGroup);
    }

    public void addVisiblePostToGroup(long groupId, long postId) {
        PostGroup group = getPostGroupById(groupId);
        Post post = postService.getVisiblePostById(postId);

        group.addPost(post);
        postGroupRepository.save(group);
    }

    public void removePostFromGroup(long groupId, long postId) {
        PostGroup group = getPostGroupById(groupId);
        Post post = postService.getPostById(postId);

        group.removePost(post);
        postGroupRepository.save(group);
    }

    public void deleteGroup(long id) throws IOException {
        PostGroup group = getPostGroupById(id);

        FileUtils.deleteDirectory(group.getDirectoryPath());
        postGroupRepository.deleteById(id);
    }
}
