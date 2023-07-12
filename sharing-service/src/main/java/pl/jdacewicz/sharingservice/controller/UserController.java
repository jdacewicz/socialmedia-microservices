package pl.jdacewicz.sharingservice.controller;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pl.jdacewicz.sharingservice.dto.UserDto;
import pl.jdacewicz.sharingservice.dto.UserRequest;
import pl.jdacewicz.sharingservice.dto.mapper.UserMapper;
import pl.jdacewicz.sharingservice.model.User;
import pl.jdacewicz.sharingservice.service.KeycloakClientService;
import pl.jdacewicz.sharingservice.service.UserService;
import pl.jdacewicz.sharingservice.util.ApiVersion;

@RestController
@RequestMapping(value = "${spring.application.api-url}" + "/users",
        produces = {ApiVersion.V1_JSON})
public class UserController {

    private final UserService userService;
    private final KeycloakClientService keycloakClientService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, KeycloakClientService keycloakClientService,
                          UserMapper userMapper) {
        this.userService = userService;
        this.keycloakClientService = keycloakClientService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable long id) {
        User user = userService.getUserById(id);
        UserRepresentation userRepresentation = keycloakClientService.getUserByEmail(user.getEmail());
        return userMapper.convertToDto(user, userRepresentation);
    }

    @PostMapping
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserRequest userRequest) {
        User user = userMapper.convertFromRequest(userRequest);
        User createdUser = userService.createUser(user);
        UserRepresentation userRepresentation = keycloakClientService.getUserByEmail(createdUser.getEmail());
        return userMapper.convertToDto(createdUser, userRepresentation);
    }

    @PutMapping
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public void uploadProfilePicture(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaim("email");
        userService.updateUser(email);
    }
}
