package pl.jdacewicz.sharingservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReactionCountDto {

    private String name;

    private String imagePath;

    private long count;
}
