package pl.jdacewicz.sharingservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PostDto {

    private long id;

    private String content;

    private String imagePath;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
    private LocalDateTime creationTime;

    private UserDto creator;

    private List<ReactionCountDto> reactions;

    private List<CommentDto> comments;

    //image url
}
