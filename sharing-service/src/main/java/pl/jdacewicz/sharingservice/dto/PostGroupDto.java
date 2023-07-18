package pl.jdacewicz.sharingservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class PostGroupDto {

    private String name;

    private String imagePath;

    private UserDto creator;

    private Set<PostDto> posts;
}
