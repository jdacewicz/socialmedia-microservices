package pl.jdacewicz.sharingservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(@NotBlank
                             @Size(max = 255)
                             String content) {
}
