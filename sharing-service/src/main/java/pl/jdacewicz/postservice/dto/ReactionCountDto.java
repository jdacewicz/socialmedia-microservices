package pl.jdacewicz.postservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReactionCountDto {

    private String name;

    //img

    private long count;
}
