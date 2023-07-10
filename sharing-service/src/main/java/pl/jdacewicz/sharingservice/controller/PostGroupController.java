package pl.jdacewicz.sharingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.jdacewicz.sharingservice.dto.PostGroupDto;
import pl.jdacewicz.sharingservice.dto.PostGroupRequest;
import pl.jdacewicz.sharingservice.dto.mapper.PostGroupMapper;
import pl.jdacewicz.sharingservice.model.PostGroup;
import pl.jdacewicz.sharingservice.service.PostGroupService;

@RestController
@RequestMapping("/api/posts/groups")
public class PostGroupController {

    private final PostGroupService postGroupService;
    private final PostGroupMapper postGroupMapper;

    @Autowired
    public PostGroupController(PostGroupService postGroupService, PostGroupMapper postGroupMapper) {
        this.postGroupService = postGroupService;
        this.postGroupMapper = postGroupMapper;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('client_user')")
    @ResponseStatus(HttpStatus.CREATED)
    public PostGroupDto getPostGroupById(@PathVariable long id) {
        PostGroup group = postGroupService.getPostGroupById(id);
        return postGroupMapper.convertToDto(group);
    }

    @PostMapping
    @PreAuthorize("hasRole('client_user')")
    @ResponseStatus(HttpStatus.CREATED)
    public PostGroupDto createGroup(@RequestBody PostGroupRequest groupRequest) {
        PostGroup group = postGroupMapper.convertFromRequest(groupRequest);
        PostGroup createdGroup = postGroupService.createGroup(group);
        return postGroupMapper.convertToDto(createdGroup);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_user')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteGroup(@PathVariable long id) {
        postGroupService.deleteGroup(id);
    }
}
