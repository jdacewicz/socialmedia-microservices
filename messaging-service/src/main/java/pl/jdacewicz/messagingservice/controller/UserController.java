package pl.jdacewicz.messagingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.jdacewicz.messagingservice.model.User;
import pl.jdacewicz.messagingservice.service.UserService;

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
    @ResponseStatus(HttpStatus.CREATED)
    public User getUser(@PathVariable long id) {
        return userService.getUserById(id);
    }
}
