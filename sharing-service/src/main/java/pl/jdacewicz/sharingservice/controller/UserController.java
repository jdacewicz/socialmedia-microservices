package pl.jdacewicz.sharingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.dto.UserDto;
import pl.jdacewicz.sharingservice.dto.mapper.UserMapper;
import pl.jdacewicz.sharingservice.model.User;
import pl.jdacewicz.sharingservice.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping(value = "${spring.application.api-url}" + "/users",
        headers = "X-API-VERSION=1",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable long id) {
        User user = userService.getUserById(id);
        return userMapper.convertToDto(user);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@AuthenticationPrincipal Jwt jwt,
                              @RequestPart MultipartFile profileImage) throws IOException {
        User createdUser = userService.createUser(jwt.getClaim("email"), profileImage);
        return userMapper.convertToDto(createdUser, jwt);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public void updateProfilePicture(@AuthenticationPrincipal Jwt jwt,
                                     @RequestPart MultipartFile profilePicture) throws IOException {
       userService.updateProfilePicture(jwt.getClaim("email"), profilePicture);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable long id) {
        userService.deleteUserById(id);
    }
}
