package pl.jdacewicz.postservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jdacewicz.postservice.exception.RecordNotFoundException;
import pl.jdacewicz.postservice.model.PostGroup;
import pl.jdacewicz.postservice.repository.PostGroupRepository;

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
}
