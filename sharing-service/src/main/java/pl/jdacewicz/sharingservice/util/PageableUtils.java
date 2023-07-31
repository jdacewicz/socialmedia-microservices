package pl.jdacewicz.sharingservice.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtils {

    public static Pageable createPageable(int page, int size, String sort, String directory) {
        if (directory == null) {
            directory = "desc";
        }
        Sort pageSort = (directory.equalsIgnoreCase(Sort.Direction.ASC.name())) ?
                Sort.by(sort).ascending() : Sort.by(sort).descending();

        return PageRequest.of(page, size, pageSort);
    }
}
