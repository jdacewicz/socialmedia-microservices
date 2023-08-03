package pl.jdacewicz.sharingservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.Reaction;
import pl.jdacewicz.sharingservice.repository.ReactionRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

class ReactionServiceTest {

    @InjectMocks
    ReactionService service;

    @Mock
    ReactionRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Given null name " +
            "When getting reactions " +
            "Then should return all reactions")
    void gettingReactionsByNullNameShouldReturnAllReactions() {
        String name = null;

        Page<Reaction> page = new PageImpl<>(List.of());
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Reaction> result = service.getReactions(name, 1, 1, "sort", "directory");

        assertEquals(page, result);
    }

    @Test
    @DisplayName("Given empty name " +
            "When getting reactions " +
            "Then should return all reactions")
    void gettingReactionsByEmptyNameShouldReturnAllReactions() {
        String name = "";

        Page<Reaction> page = new PageImpl<>(List.of());
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Reaction> result = service.getReactions(name, 1, 1, "sort", "directory");

        assertEquals(page, result);
    }

    @Test
    @DisplayName("Given name " +
            "When getting reactions " +
            "Then should return all reactions with name")
    void gettingReactionsByNameShouldReturnReactionsWithName() {
        String name = "name";

        Page<Reaction> page = new PageImpl<>(List.of());
        when(repository.findAllByName(any(String.class), any(Pageable.class))).thenReturn(page);

        Page<Reaction> result = service.getReactions(name, 1, 1, "sort", "directory");

        assertEquals(page, result);
    }

    @Test
    @DisplayName("Given existing id " +
            "When getting reaction " +
            "Then should return reaction")
    void gettingReactionByExistingIdShouldReturnReaction() {
        int id = 1;

        Reaction reaction = new Reaction();
        when(repository.findById(id)).thenReturn(Optional.of(reaction));

        Reaction result = service.getReactionById(id);

        assertEquals(reaction, result);
    }

    @Test
    @DisplayName("Given not existing id " +
            "When getting reaction " +
            "Then should throw RecordNotFoundException")
    void gettingReactionByNotExistingIdShouldThrowException() {
        int id = 1;

        when(repository.findById(id)).thenThrow(RecordNotFoundException.class);

        assertThrows(RecordNotFoundException.class,
                () -> service.getReactionById(id));
    }
}