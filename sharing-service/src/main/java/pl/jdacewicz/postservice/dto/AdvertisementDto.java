package pl.jdacewicz.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementDto {

   private String name;

   private String content;

   private List<ReactionCountDto> reactions;

   private List<CommentDto> comments;
}
