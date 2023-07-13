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

    public PostGroup createGroup(String userEmail, PostGroup postGroup) {
        User user = userService.getUserByEmail(userEmail);
        postGroup.setCreator(user);
        return postGroupRepository.save(postGroup);
    }

    public void deleteGroup(long id) {
        postGroupRepository.deleteById(id);
    }
}
