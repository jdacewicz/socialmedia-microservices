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
import pl.jdacewicz.sharingservice.model.Reaction;
import pl.jdacewicz.sharingservice.repository.ReactionRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ReactionServiceTest {

    @InjectMocks
    ReactionService reactionService;

    @Mock
    ReactionRepository reactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given null name " +
            "When getting advertisements " +
            "Then should return all advertisements")
    void gettingAdvertisementsByNullNameShouldReturnAllAdvertisements() {
        String name = null;

        List<Reaction> list = List.of(new Reaction());
        Page<Reaction> page = new PageImpl<>(list);
        when(reactionRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Reaction> returnedPage = reactionService.getReactions(name, 1 , 1,
                "id", "asc");
        assertFalse(returnedPage.isEmpty());
    }

    @Test
    @DisplayName("Given empty name " +
            "When getting advertisements " +
            "Then should return all advertisements")
    void gettingAdvertisementsByEmptyNameShouldReturnAllAdvertisements() {
        String name = "";

        List<Reaction> list = List.of(new Reaction());
        Page<Reaction> page = new PageImpl<>(list);
        when(reactionRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Reaction> returnedPage = reactionService.getReactions(name, 1 , 1,
                "id", "asc");
        assertFalse(returnedPage.isEmpty());
    }

    @Test
    @DisplayName("Given not null and not empty name " +
            "When getting advertisements " +
            "Then should return advertisements with name")
    void gettingAdvertisementsByNotNullAndNotEmptyNameShouldReturnAllAdvertisementsWithName() {
        String name = "test";

        Page<Reaction> page = Page.empty();
        when(reactionRepository.findAllByName(any(String.class), any(Pageable.class)))
                .thenReturn(page);

        Page<Reaction> returnedPage = reactionService.getReactions(name, 1 , 1,
                "id", "asc");
        assertTrue(returnedPage.isEmpty());
    }
}