package pl.jdacewicz.sharingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReactionDto {

    private String name;
    private String imagePath;
}
