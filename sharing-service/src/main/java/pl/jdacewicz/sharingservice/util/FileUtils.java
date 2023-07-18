package pl.jdacewicz.sharingservice.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileUtils {

    public static void saveFile(MultipartFile file, String fileName, String fileDir) throws IOException {
        Path uploadPath = Paths.get(fileDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        InputStream inputStream = file.getInputStream();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void deleteFile(String fileDir, String fileName) throws IOException {
        Path deletePath = Paths.get(fileDir);

        if (Files.exists(deletePath)) {
            Path filePath = deletePath.resolve(fileName);
            Files.deleteIfExists(filePath);
        }
    }

    public static void deleteDirectory(String directory) throws IOException {
        Path deletePath = Paths.get(directory);

        Files.deleteIfExists(deletePath);
    }

    public static String generateFileName(String originalName) {
        int lastDotIndex = originalName.lastIndexOf('.');
        String extension = originalName.substring(lastDotIndex);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(formatter) + extension;
    }
}
