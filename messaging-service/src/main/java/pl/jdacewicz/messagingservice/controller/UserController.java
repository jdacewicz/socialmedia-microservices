package pl.jdacewicz.messagingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.messagingservice.model.User;
import pl.jdacewicz.messagingservice.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping(value = "${spring.application.api-url}" + "/users",
        headers = "X-API-VERSION=1",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@AuthenticationPrincipal Jwt jwt,
                           @RequestPart MultipartFile profilePicture) throws IOException {
        return userService.createUser(jwt.getClaim("email"), profilePicture);
    }

    @PutMapping(value = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateProfilePicture(@AuthenticationPrincipal Jwt jwt,
                                     @RequestPart MultipartFile profilePicture) throws IOException {
        userService.updateProfilePicture(jwt.getClaim("email"), profilePicture);
    }
}
