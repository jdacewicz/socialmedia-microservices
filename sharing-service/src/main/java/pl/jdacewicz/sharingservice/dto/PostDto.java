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

    //UserDto

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
    private LocalDateTime creationTime;

    private List<ReactionCountDto> reactions;

    private List<CommentDto> comments;

    //image url
}
