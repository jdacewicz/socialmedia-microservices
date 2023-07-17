package pl.jdacewicz.sharingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.PostGroup;
import pl.jdacewicz.sharingservice.model.User;
import pl.jdacewicz.sharingservice.repository.PostGroupRepository;

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

    public PostGroup createGroup(String userEmail, String name, String fileName) {
        User user = userService.getUserByEmail(userEmail);
        PostGroup postGroup = PostGroup.builder()
                .name(name)
                .image(fileName)
                .creator(user)
                .build();
        return postGroupRepository.save(postGroup);
    }

    public PostGroup updateGroup(long id, String name) {
        return postGroupRepository.findById(id)
                .map(group -> {
                    group.setName(name);
                    return postGroupRepository.save(group);
                }).orElseThrow(() -> new RecordNotFoundException("Could not find group with id: " + id));
    }

    public void deleteGroup(long id) {
        postGroupRepository.deleteById(id);
    }
}
