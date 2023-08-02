package pl.jdacewicz.sharingservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.Post;
import pl.jdacewicz.sharingservice.repository.PostRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class PostServiceTest {

    @InjectMocks
    PostService service;

    @Mock
    PostRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given existing id " +
            "When getting post " +
            "Then should return post")
    void gettingPostByExistingIdShouldReturnPost() {
        long id = 1;

        Post post = new Post();
        when(repository.findById(id)).thenReturn(Optional.of(post));

        Post result = service.getPostById(id);

        assertEquals(post, result);
    }

    @Test
    @DisplayName("Given not existing id " +
            "When getting post " +
            "Then should throw RecordNotFoundException")
    void gettingPostByNotExistingIdShouldThrowException() {
        long id = 1;

        when(repository.findById(id)).thenThrow(RecordNotFoundException.class);

        assertThrows(RecordNotFoundException.class,
                () -> service.getPostById(id));
    }

    @Test
    @DisplayName("Given existing id " +
            "When getting post " +
            "Then should return post")
    void gettingVisiblePostByExistingIdShouldReturnPost() {
        long id = 1;

        Post post = new Post();
        when(repository.findByIdAndVisibleIs(id, true)).thenReturn(Optional.of(post));

        Post result = service.getVisiblePostById(id);

        assertEquals(post, result);
    }

    @Test
    @DisplayName("Given not existing id " +
            "When getting post " +
            "Then should throw RecordNotFoundException")
    void gettingVisiblePostByNotExistingIdShouldThrowException() {
        long id = 1;

        when(repository.findByIdAndVisibleIs(id, true)).thenThrow(RecordNotFoundException.class);

        assertThrows(RecordNotFoundException.class,
                () -> service.getVisiblePostById(id));
    }
}