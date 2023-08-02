package pl.jdacewicz.sharingservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.Comment;
import pl.jdacewicz.sharingservice.repository.CommentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CommentServiceTest {

    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given existing id " +
            "When getting comment " +
            "Then should return comment")
    void gettingCommentByExistingIdShouldReturnComment() {
        long id = 1;

        Comment comment = new Comment();
        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));

        Comment result = commentService.getCommentById(id);
        assertEquals(comment, result);
    }

    @Test
    @DisplayName("Given not existing id " +
            "When getting comment " +
            "Then should throw RecordNotFoundException")
    void gettingCommentByNotExistingIdShouldThrowException() {
        long id = 1;

        when(commentRepository.findById(id)).thenThrow(RecordNotFoundException.class);

        assertThrows(RecordNotFoundException.class,
                () -> commentService.getCommentById(id));
    }

    @Test
    @DisplayName("Given existing id " +
            "When getting visible comment " +
            "Then should return comment")
    void gettingVisibleCommentByExistingIdShouldReturnComment() {
        long id = 1;

        Comment comment = new Comment();
        when(commentRepository.findByIdAndVisible(id, true)).thenReturn(Optional.of(comment));

        Comment result = commentService.getVisibleCommentById(id);
        assertEquals(comment, result);
    }

    @Test
    @DisplayName("Given not existing id " +
            "When getting visible comment " +
            "Then should throw RecordNotFoundException")
    void gettingVisibleCommentByNotExistingIdShouldThrowException() {
        long id = 1;

        when(commentRepository.findByIdAndVisible(id, true)).thenThrow(RecordNotFoundException.class);

        assertThrows(RecordNotFoundException.class,
                () -> commentService.getVisibleCommentById(id));
    }
}