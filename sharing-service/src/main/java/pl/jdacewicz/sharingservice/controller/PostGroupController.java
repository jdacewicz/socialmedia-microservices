package pl.jdacewicz.sharingservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.dto.PostGroupDto;
import pl.jdacewicz.sharingservice.dto.PostGroupRequest;
import pl.jdacewicz.sharingservice.dto.mapper.PostGroupMapper;
import pl.jdacewicz.sharingservice.model.PostGroup;
import pl.jdacewicz.sharingservice.service.PostGroupService;

import java.io.IOException;

@RestController
@RequestMapping(value = "${spring.application.api-url}" + "/posts/groups",
        headers = "X-API-VERSION=1",
        produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.CREATED)
    public PostGroupDto createGroup(@AuthenticationPrincipal Jwt jwt,
                                    @Valid @RequestPart PostGroupRequest request,
                                    @RequestPart MultipartFile image) throws IOException {
        PostGroup createdGroup = postGroupService.createGroup(jwt.getClaim("email"), request, image);
        return postGroupMapper.convertToDto(createdGroup);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('user')")
    public PostGroupDto updateGroup(@PathVariable long id,
                                    @Valid @RequestPart PostGroupRequest request,
                                    @RequestPart MultipartFile image) throws IOException {
        PostGroup updatedGroup = postGroupService.updateGroup(id, request, image);
        return postGroupMapper.convertToDto(updatedGroup);
    }

    @PutMapping("/{groupId}/posts/{postId}/add")
    @PreAuthorize("hasRole('user')")
    public void addPostToGroup(@PathVariable long postId,
                               @PathVariable long groupId) {
        postGroupService.addVisiblePostToGroup(groupId, postId);
    }
    @PutMapping("/{groupId}/posts/{postId}/remove")
    @PreAuthorize("hasRole('user')")
    public void removePostFromGroup(@PathVariable long postId,
                                    @PathVariable long groupId) {
        postGroupService.removePostFromGroup(groupId, postId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('user')")
    public void deleteGroup(@PathVariable long id) throws IOException {
        postGroupService.deleteGroup(id);
    }
}
