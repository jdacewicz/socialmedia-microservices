package pl.jdacewicz.sharingservice.dto.mapper;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;
import pl.jdacewicz.sharingservice.dto.UserDto;
import pl.jdacewicz.sharingservice.model.User;

@Component
public class UserMapper {

    public UserDto convertToDto(User user, UserRepresentation userRepresentation) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(userRepresentation.getFirstName())
                .lastname(userRepresentation.getLastName())
                .build();
    }
}
