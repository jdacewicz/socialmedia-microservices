package pl.jdacewicz.sharingservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jdacewicz.sharingservice.exception.NotUniqueDataException;
import pl.jdacewicz.sharingservice.model.User;
import pl.jdacewicz.sharingservice.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given user with unique email " +
            "When creating user" +
            "Then should return created user")
    void creatingUserByNotUniqueEmailShouldReturnUser() {
        User user = new User();
        user.setEmail("test@test.com");

        when(userRepository.save(user)).thenReturn(user);

        assertDoesNotThrow(() -> userService.createUser(user));
    }

    @Test
    @DisplayName("Given user with unique not unique email " +
            "When creating user " +
            "Then should throw NotUniqueDataException")
    void creatingUserByUniqueEmailShouldThrowNotUniqueDataException() {
        User user = new User();
        user.setEmail("test@test.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(NotUniqueDataException.class,
                () -> userService.createUser(user));
    }
}