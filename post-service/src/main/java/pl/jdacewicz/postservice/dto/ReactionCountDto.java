package pl.jdacewicz.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReactionCountDto {

    private String name;

    //img

    private long count;
}
