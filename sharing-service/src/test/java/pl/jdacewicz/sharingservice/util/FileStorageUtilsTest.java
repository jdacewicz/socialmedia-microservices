package pl.jdacewicz.sharingservice.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileStorageUtilsTest {

    @Test
    @DisplayName("Given id, null image, directoryPath " +
            "When getting image path " +
            "Then should return null")
    void gettingImagePathByIdAndNullImageAndDirectoryPathShouldReturnNull() {
        long id = 1;
        String image = null;
        String directoryPath = "tmp";

        String result = FileStorageUtils.getImagePath(id, image, directoryPath);

        assertEquals("", result);
    }

    @Test
    @DisplayName("Given id, empty image, directoryPath " +
            "When getting image path " +
            "Then should return null")
    void gettingImagePathByIdAndEmptyImageAndDirectoryPathShouldReturnNull() {
        long id = 1;
        String image = "";
        String directoryPath = "tmp";

        String result = FileStorageUtils.getImagePath(id, image, directoryPath);

        assertEquals("", result);
    }

    @Test
    @DisplayName("Given id, image, null directoryPath " +
            "When getting image path " +
            "Then should return null")
    void gettingImagePathByIdAndImageAndNullDirectoryPathShouldReturnNull() {
        long id = 1;
        String image = "image.png";
        String directoryPath = null;

        String result = FileStorageUtils.getImagePath(id, image, directoryPath);

        assertEquals("", result);
    }

    @Test
    @DisplayName("Given id, image, empty directoryPath " +
            "When getting image path " +
            "Then should return null")
    void gettingImagePathByIdAndImageAndDirectoryPathShouldReturnNull() {
        long id = 1;
        String image = "image.png";
        String directoryPath = "";

        String result = FileStorageUtils.getImagePath(id, image, directoryPath);

        assertEquals("", result);
    }

    @Test
    @DisplayName("Given id, image, directoryPath " +
            "When getting image path " +
            "Then should return string path")
    void gettingImagePathByIdAndImageAndDirectoryPathShouldReturnPathAsString() {
        long id = 1;
        String image = "image.png";
        String directoryPath = "tmp";

        String result = FileStorageUtils.getImagePath(id, image, directoryPath);

        String expected = directoryPath + "/" + id + "/" + image;
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Given id, null directory " +
            "When getting directory path " +
            "Then should return null")
    void gettingDirectoryPathByIdAndNullDirectoryShouldReturnNull() {
        long id = 1;
        String directoryPath = null;

        String result = FileStorageUtils.getDirectoryPath(id, directoryPath);

        assertEquals("", result);
    }

    @Test
    @DisplayName("Given id, empty directory " +
            "When getting directory path " +
            "Then should return null")
    void gettingDirectoryPathByIdAndEmptyDirectoryShouldReturnNull() {
        long id = 1;
        String directoryPath = "";

        String result = FileStorageUtils.getDirectoryPath(id, directoryPath);

        assertEquals("", result);
    }

    @Test
    @DisplayName("Given id, directory " +
            "When getting directory path " +
            "Then should return string path")
    void gettingDirectoryPathByIdAndDirectoryShouldReturnPathAsString() {
        long id = 1;
        String directoryPath = "tmp";

        String result = FileStorageUtils.getDirectoryPath(id, directoryPath);

        String expected = directoryPath + "/" + id;
        assertEquals(expected, result);
    }
}