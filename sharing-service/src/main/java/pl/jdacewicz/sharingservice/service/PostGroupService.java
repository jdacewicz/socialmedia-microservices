package pl.jdacewicz.sharingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.PostGroup;
import pl.jdacewicz.sharingservice.model.User;
import pl.jdacewicz.sharingservice.repository.PostGroupRepository;
import pl.jdacewicz.sharingservice.util.FileUtils;

import java.io.IOException;

@Service
public class PostGroupService {

    private final PostGroupRepository postGroupRepository;
    private final UserService userService;

    @Autowired
    public PostGroupService(PostGroupRepository postGroupRepository, UserService userService) {
        this.postGroupRepository = postGroupRepository;
        this.userService = userService;
    }

    public PostGroup getPostGroupById(long id) {
        return postGroupRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Could not find group with id: " + id));
    }

    public PostGroup createGroup(String userEmail, String name, MultipartFile image) throws IOException {
        User user = userService.getUserByEmail(userEmail);
        String newFileName = FileUtils.generateFileName(image.getOriginalFilename());

        PostGroup postGroup = PostGroup.builder()
                .name(name)
                .image(newFileName)
                .creator(user)
                .build();
        PostGroup createdGroup = postGroupRepository.save(postGroup);

        FileUtils.saveFile(image, newFileName, createdGroup.getDirectoryPath());
        return createdGroup;
    }

    public PostGroup updateGroup(long id, String name, MultipartFile image) throws IOException {
        PostGroup postGroup = postGroupRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Could not find group with id: " + id));

        postGroup.setName(name);
        FileUtils.saveFile(image, postGroup.getImage(), postGroup.getDirectoryPath());
        return postGroupRepository.save(postGroup);
    }

    public void deleteGroup(long id) {
        postGroupRepository.deleteById(id);
    }
}
