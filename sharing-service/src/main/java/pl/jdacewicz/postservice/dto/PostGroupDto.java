package pl.jdacewicz.postservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class PostGroupDto {

    private String name;

    private Set<PostDto> posts;
}
