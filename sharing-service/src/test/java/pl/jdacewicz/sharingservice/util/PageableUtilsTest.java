package pl.jdacewicz.sharingservice.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PageableUtilsTest {

    @Test
    @DisplayName("Given ascending directory " +
            "When creating pageable " +
            "Then should return sorted ascending Pageable")
    void creatingPageableByAscendingDirectoryShouldReturnProperPageable() {
        String directory = "asc";

        String sort = "sort";
        Pageable pageable = PageableUtils.createPageable(1, 1, sort, directory);

        assertEquals(Sort.by(sort).ascending().toString(), pageable.getSort().toString());
    }

    @Test
    @DisplayName("Given descending directory " +
            "When creating pageable " +
            "Then should return sorted descending Pageable")
    void creatingPageableByDescendingDirectoryShouldReturnProperPageable() {
        String directory = "desc";

        String sort = "sort";
        Pageable pageable = PageableUtils.createPageable(1, 1, sort, directory);

        assertEquals(Sort.by(sort).descending().toString(), pageable.getSort().toString());
    }

    @Test
    @DisplayName("Given unknown directory " +
            "When creating pageable " +
            "Then should return sorted descending Pageable")
    void creatingPageableByUnknownDirectoryShouldReturnProperPageable() {
        String directory = "test";

        String sort = "sort";
        Pageable pageable = PageableUtils.createPageable(1, 1, sort, directory);

        assertEquals(Sort.by(sort).descending().toString(), pageable.getSort().toString());
    }

    @Test
    @DisplayName("Given empty directory " +
            "When creating pageable " +
            "Then should return sorted descending Pageable")
    void creatingPageableByEmptyDirectoryShouldReturnProperPageable() {
        String directory = "";

        String sort = "sort";
        Pageable pageable = PageableUtils.createPageable(1, 1, sort, directory);

        assertEquals(Sort.by(sort).descending().toString(), pageable.getSort().toString());
    }

    @Test
    @DisplayName("Given null directory " +
            "When creating pageable " +
            "Then should return sorted descending Pageable")
    void creatingPageableByNullDirectoryShouldReturnProperPageable() {
        String directory = null;

        String sort = "sort";
        Pageable pageable = PageableUtils.createPageable(1, 1, sort, directory);

        assertEquals(Sort.by(sort).descending().toString(), pageable.getSort().toString());
    }
}