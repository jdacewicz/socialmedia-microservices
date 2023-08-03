package pl.jdacewicz.sharingservice.util;

import org.apache.commons.lang.StringUtils;

public class FileStorageUtils {

    public static String getImagePath(long id, String image, String directoryPath) {
        if (StringUtils.isBlank(image) || StringUtils.isBlank(directoryPath)) {
            return "";
        }
        return getDirectoryPath(id, directoryPath) + "/" + image;
    }

    public static String getDirectoryPath(long id, String directoryPath) {
        if (StringUtils.isBlank(directoryPath)) {
            return "";
        }
        return directoryPath + "/" + id;
    }
}
