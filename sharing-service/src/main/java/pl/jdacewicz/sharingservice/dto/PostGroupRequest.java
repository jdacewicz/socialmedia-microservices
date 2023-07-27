package pl.jdacewicz.sharingservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostGroupRequest(@NotBlank
                               @Size(max = 32)
                               String name) {
}
