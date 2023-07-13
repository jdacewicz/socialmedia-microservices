package pl.jdacewicz.sharingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pl.jdacewicz.sharingservice.dto.PostGroupDto;
import pl.jdacewicz.sharingservice.dto.PostGroupRequest;
import pl.jdacewicz.sharingservice.dto.mapper.PostGroupMapper;
import pl.jdacewicz.sharingservice.model.PostGroup;
import pl.jdacewicz.sharingservice.service.PostGroupService;
import pl.jdacewicz.sharingservice.util.ApiVersion;

@RestController
@RequestMapping(value = "${spring.application.api-url}" + "/posts/groups",
        produces = {ApiVersion.V1_JSON})
public class PostGroupController {

    private final PostGroupService postGroupService;
    private final PostGroupMapper postGroupMapper;

    @Autowired
    public PostGroupController(PostGroupService postGroupService, PostGroupMapper postGroupMapper) {
        this.postGroupService = postGroupService;
        this.postGroupMapper = postGroupMapper;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.CREATED)
    public PostGroupDto getPostGroupById(@PathVariable long id) {
        PostGroup group = postGroupService.getPostGroupById(id);
        return postGroupMapper.convertToDto(group);
    }

    @PostMapping
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.CREATED)
    public PostGroupDto createGroup(@AuthenticationPrincipal Jwt jwt,
                                    @RequestBody PostGroupRequest groupRequest) {
        PostGroup group = postGroupMapper.convertFromRequest(groupRequest);
        String userEmail = jwt.getClaim("email");
        PostGroup createdGroup = postGroupService.createGroup(userEmail, group);
        return postGroupMapper.convertToDto(createdGroup);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteGroup(@PathVariable long id) {
        postGroupService.deleteGroup(id);
    }
}
