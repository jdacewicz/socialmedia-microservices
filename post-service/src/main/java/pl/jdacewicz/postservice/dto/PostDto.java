package pl.jdacewicz.postservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class PostDto {

    private long id;

    //UserDto

    //List<ReactionsDto>

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
    private LocalDateTime creationTime;

    private String content;

    //image url
}
