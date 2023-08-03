package pl.jdacewicz.sharingservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.User;
import pl.jdacewicz.sharingservice.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @InjectMocks
    UserService service;

    @Mock
    UserRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Given existing id " +
            "When getting user " +
            "Then should return user")
    void gettingUserByExistingIdShouldReturnUser() {
        long id = 1;

        User user = new User();
        when(repository.findById(id)).thenReturn(Optional.of(user));

        User result = service.getUserById(id);

        assertEquals(user, result);
    }

    @Test
    @DisplayName("Given not existing id " +
            "When getting user " +
            "Then should throw RecordNotFoundException")
    void gettingUserByNotExistingIdShouldThrowException() {
        long id = 1;

        when(repository.findById(id)).thenThrow(RecordNotFoundException.class);

        assertThrows(RecordNotFoundException.class,
                () -> service.getUserById(id));
    }

    @Test
    @DisplayName("Given existing email " +
            "When getting user " +
            "Then should return user")
    void gettingUserByExistingEmailShouldReturnUser() {
        String email = "test@test.com";

        User user = new User();
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = service.getUserByEmail(email);

        assertEquals(user, result);
    }

    @Test
    @DisplayName("Given not existing email " +
            "When getting user " +
            "Then should throw RecordNotFoundException")
    void gettingUserByNotExistingEmailShouldThrowException() {
        String email = "test@test.com";

        when(repository.findByEmail(email)).thenThrow(RecordNotFoundException.class);

        assertThrows(RecordNotFoundException.class,
                () -> service.getUserByEmail(email));
    }
}