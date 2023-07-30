package pl.jdacewicz.sharingservice.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.exception.InvalidPathException;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {
    @AfterEach
    void cleanUp() throws IOException {
        File directory = new File("tmp");
        org.apache.commons.io.FileUtils.deleteDirectory(directory);
    }

    @Test
    @DisplayName("Given file, fileDir and null fileName " +
            "When saving file " +
            "Then should throw InvalidPathException")
      void savingFileByFileAndFileDirAndNullFileNameShouldThrowException() {
        MultipartFile file = new MockMultipartFile("test.txt", "test".getBytes());
        String fileDir = "tmp";
        String fileName = null;

        assertThrows(InvalidPathException.class,
                () -> FileUtils.saveFile(file, fileName, fileDir));
    }

    @Test
    @DisplayName("Given file, fileName and null fileDir " +
            "When saving file " +
            "Then should throw InvalidPathException")
    void savingFileByFileAndNullFileDirAndFileNameShouldThrowException() {
        MultipartFile file = new MockMultipartFile("test.txt", "test".getBytes());
        String fileDir = null;
        String fileName = "newName.txt";

        assertThrows(InvalidPathException.class,
                () -> FileUtils.saveFile(file, fileName, fileDir));
    }

    @Test
    @DisplayName("Given file, fileDir and empty fileName " +
            "When saving file " +
            "Then should throw InvalidPathException")
    void savingFileByFileAndFileDirAndEmptyFileNameShouldThrowException() {
        MultipartFile file = new MockMultipartFile("test.txt", "test".getBytes());
        String fileDir = "tmp";
        String fileName = "";

        assertThrows(InvalidPathException.class,
                () -> FileUtils.saveFile(file, fileName, fileDir));
    }

    @Test
    @DisplayName("Given file, fileName and empty fileDir " +
            "When saving file " +
            "Then should throw InvalidPathException")
    void savingFileByFileAndEmptyFileDirAndFileNameShouldThrowException() {
        MultipartFile file = new MockMultipartFile("test.txt", "test".getBytes());
        String fileDir = "";
        String fileName = "newName.txt";

        assertThrows(InvalidPathException.class,
                () -> FileUtils.saveFile(file, fileName, fileDir));
    }

    @Test
    @DisplayName("Given file, fileName and fileDir " +
            "When saving file " +
            "Then should save file")
    void savingFileByFileAndFileDirAndFileNameShouldSaveFile() throws IOException {
        MultipartFile file = new MockMultipartFile("test.txt", "test".getBytes());
        String fileDir = "tmp";
        String fileName = "newName.txt";

        FileUtils.saveFile(file, fileName, fileDir);

        assertTrue(new File(fileDir + "/" + fileName).isFile());
    }

    @Test
    @DisplayName("Given fileName and null fileDir " +
            "When deleting file " +
            "Then should throw InvalidPathException")
    void deletingFileByFileNameAndNullFileDirShouldThrowException() {
        String fileDir = null;
        String fileName = "newName.txt";

        assertThrows(InvalidPathException.class,
                () -> FileUtils.deleteFile(fileDir, fileName));
    }

    @Test
    @DisplayName("Given fileName and empty fileDir " +
            "When deleting file " +
            "Then should throw InvalidPathException")
    void deletingFileByFileNameAndEmptyFileDirShouldThrowException() {
        String fileDir = "";
        String fileName = "newName.txt";

        assertThrows(InvalidPathException.class,
                () -> FileUtils.deleteFile(fileDir, fileName));
    }

    @Test
    @DisplayName("Given fileDir and null fileName " +
            "When deleting file " +
            "Then should throw InvalidPathException")
    void deletingFileByFileDirAndNullFileNameShouldThrowException() {
        String fileDir = "tmp";
        String fileName = null;

        assertThrows(InvalidPathException.class,
                () -> FileUtils.deleteFile(fileDir, fileName));
    }

    @Test
    @DisplayName("Given fileDir and empty fileName " +
            "When deleting file " +
            "Then should throw InvalidPathException")
    void deletingFileByFileDirAndEmptyFileNameShouldThrowException() {
        String fileDir = "tmp";
        String fileName = "";

        assertThrows(InvalidPathException.class,
                () -> FileUtils.deleteFile(fileDir, fileName));
    }

    @Test
    @DisplayName("Given fileName and fileDir " +
            "When deleting file " +
            "Then should delete file")
    void deletingFileByFileDirAndFileNameShouldDeleteFile() throws IOException {
        String fileDir = "tmp";
        String fileName = "newName.txt";

        MultipartFile testFile = new MockMultipartFile("testFile", "content".getBytes());
        File directory = new File(fileDir + "/" + fileName);
        org.apache.commons.io.FileUtils.copyInputStreamToFile(testFile.getInputStream(), directory);

        FileUtils.deleteFile(fileDir, fileName);

        assertFalse(new File(fileDir + "/" + fileName).isFile());
    }
}