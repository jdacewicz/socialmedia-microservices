package pl.jdacewicz.sharingservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private long id;
    private String email;
    private String firstname;
    private String lastname;
    //img
}
