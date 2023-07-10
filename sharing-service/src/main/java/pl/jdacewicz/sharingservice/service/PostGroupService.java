package pl.jdacewicz.sharingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.PostGroup;
import pl.jdacewicz.sharingservice.repository.PostGroupRepository;

@Service
public class PostGroupService {

    private final PostGroupRepository postGroupRepository;

    @Autowired
    public PostGroupService(PostGroupRepository postGroupRepository) {
        this.postGroupRepository = postGroupRepository;
    }

    public PostGroup getPostGroupById(long id) {
        return postGroupRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Could not find group with id: " + id));
    }

    public PostGroup createGroup(PostGroup postGroup) {
        return postGroupRepository.save(postGroup);
    }

    public void deleteGroup(long id) {
        postGroupRepository.deleteById(id);
    }
}
