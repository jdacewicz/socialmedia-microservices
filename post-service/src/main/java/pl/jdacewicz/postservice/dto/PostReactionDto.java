package pl.jdacewicz.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PostReactionDto {

    private String name;
    //img
    private long count;
}
