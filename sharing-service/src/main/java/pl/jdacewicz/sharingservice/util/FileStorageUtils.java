package pl.jdacewicz.sharingservice.util;

public class FileStorageUtils {

    public static String getImagePath(long id, String image, String directoryPath) {
        if (image == null) {
            return null;
        }
        return getDirectoryPath(id, directoryPath) + "/" + image;
    }

    public static String getDirectoryPath(long id, String directoryPath) {
        return directoryPath + "/" + id;
    }
}
