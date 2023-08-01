package pl.jdacewicz.sharingservice.util;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.exception.InvalidFileNameException;
import pl.jdacewicz.sharingservice.exception.InvalidPathException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static void saveFile(MultipartFile file, String fileName, String fileDir) throws IOException {
        if (StringUtils.isBlank(fileDir) || StringUtils.isBlank(fileName)) {
            throw new InvalidPathException();
        }
        InputStream inputStream = file.getInputStream();
        File directory = new File(fileDir + "/" + fileName);
        org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, directory);
    }

    public static void deleteFile(String fileDir, String fileName) throws IOException {
        if (StringUtils.isBlank(fileDir) || StringUtils.isBlank(fileName)) {
            throw new InvalidPathException();
        }
        File directory = new File(fileDir + "/" + fileName);
        org.apache.commons.io.FileUtils.delete(directory);
    }

    public static void deleteDirectory(String folderDir) throws IOException {
        if (StringUtils.isBlank(folderDir)) {
            throw new InvalidPathException();
        }
        File directory = new File(folderDir);
        org.apache.commons.io.FileUtils.deleteDirectory(directory);
    }

    public static String generateFileName(String originalName) {
        String extension = "";
        if (StringUtils.isBlank(originalName)) {
            throw new InvalidFileNameException();
        }
        int lastDotIndex = originalName.lastIndexOf('.');
        if (lastDotIndex >= 0) {
            extension = originalName.substring(lastDotIndex);
        }

        return RandomStringUtils.randomAlphanumeric(16) + extension;
    }
}
