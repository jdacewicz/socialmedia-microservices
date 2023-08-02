package pl.jdacewicz.sharingservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.PostGroup;
import pl.jdacewicz.sharingservice.repository.PostGroupRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class PostGroupServiceTest {

    @InjectMocks
    PostGroupService service;

    @Mock
    PostGroupRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given existing id " +
            "When getting post group " +
            "Then should return post group")
    void gettingPostGroupByExistingIdShouldReturnPostGroup() {
        long id = 1;

        PostGroup group = new PostGroup();
        when(repository.findById(id)).thenReturn(Optional.of(group));

        PostGroup result = service.getPostGroupById(id);

        assertEquals(group, result);
    }

    @Test
    @DisplayName("Given not existing id " +
            "When getting post group " +
            "Then should throw RecordNotFoundException")
    void gettingPostGroupByNotExistingIdShouldThrowException() {
        long id = 1;

        when(repository.findById(id)).thenThrow(RecordNotFoundException.class);

        assertThrows(RecordNotFoundException.class,
                () -> service.getPostGroupById(id));
    }
}