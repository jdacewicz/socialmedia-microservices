package pl.jdacewicz.sharingservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdvertisementRequest(@NotBlank
                                   @Email
                                   String userEmail,

                                   @NotBlank
                                   @Size(min = 3, max = 32)
                                   String name,

                                   @NotBlank
                                   @Size(max = 255)
                                   String content) {
}
