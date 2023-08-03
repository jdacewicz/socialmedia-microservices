package pl.jdacewicz.sharingservice.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileValidatorTest {

    private static FileValidator validator;

    ConstraintValidatorContext context;

    @BeforeAll
    static void setUp() {
        validator = new FileValidator();
    }

    @Test
    @DisplayName("Given file with null contentType " +
            "When checking is valid " +
            "Then should return false")
    void checkingIsValidByMultipartFileWithNullContentTypeShouldReturnFalse() {
        MultipartFile file = new MockMultipartFile("name", "originalName",
                null, "".getBytes());

        boolean result = validator.isValid(file, context);

        assertFalse(result);
    }

    @Test
    @DisplayName("Given file with empty contentType " +
            "When checking is valid " +
            "Then should return false")
    void checkingIsValidByMultipartFileWithEmptyContentTypeShouldReturnFalse() {
        MultipartFile file = new MockMultipartFile("name", "originalName",
                "", "".getBytes());

        boolean result = validator.isValid(file, context);

        assertFalse(result);
    }

    @Test
    @DisplayName("Given file with text/plain contentType " +
            "When checking is valid " +
            "Then should return false")
    void checkingIsValidByMultipartFileWithTextPlainContentTypeShouldReturnFalse() {
        MultipartFile file = new MockMultipartFile("name", "originalName",
                "text/plain", "".getBytes());

        boolean result = validator.isValid(file, context);

        assertFalse(result);
    }

    @Test
    @DisplayName("Given file with image/png contentType " +
            "When checking is valid " +
            "Then should return true")
    void checkingIsValidByMultipartFileWithImagePngContentTypeShouldReturnTrue() {
        MultipartFile file = new MockMultipartFile("name", "originalName",
                "image/png", "".getBytes());

        boolean result = validator.isValid(file, context);

        assertTrue(result);
    }

    @Test
    @DisplayName("Given file with image/jpg contentType " +
            "When checking is valid " +
            "Then should return true")
    void checkingIsValidByMultipartFileWithImageJpgContentTypeShouldReturnTrue() {
        MultipartFile file = new MockMultipartFile("name", "originalName",
                "image/jpg", "".getBytes());

        boolean result = validator.isValid(file, context);

        assertTrue(result);
    }

    @Test
    @DisplayName("Given file with image/jpeg contentType " +
            "When checking is valid " +
            "Then should return true")
    void checkingIsValidByMultipartFileWithImageJpegContentTypeShouldReturnTrue() {
        MultipartFile file = new MockMultipartFile("name", "originalName",
                "image/jpeg", "".getBytes());

        boolean result = validator.isValid(file, context);

        assertTrue(result);
    }
}