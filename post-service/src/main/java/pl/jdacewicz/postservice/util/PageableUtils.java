package pl.jdacewicz.postservice.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtils {

    public static Pageable createPageable(int page, int size, String sort, String directory) {
        Sort pageSort = directory.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sort).descending() : Sort.by(sort).ascending();

        return PageRequest.of(page, size, pageSort);
    }
}
