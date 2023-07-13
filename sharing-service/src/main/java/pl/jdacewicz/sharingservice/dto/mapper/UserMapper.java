package pl.jdacewicz.sharingservice.dto.mapper;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import pl.jdacewicz.sharingservice.dto.UserDto;
import pl.jdacewicz.sharingservice.dto.UserRequest;
import pl.jdacewicz.sharingservice.model.User;

@Component
public class UserMapper {

    public UserDto convertToDto(User user, UserRepresentation userRepresentation) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(userRepresentation.getFirstName())
                .lastname(userRepresentation.getLastName())
                .profilePicture(user.getProfilePicture())
                .build();
    }

    public UserDto convertToDto(User user, Jwt jwt) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(jwt.getClaim("given_name"))
                .lastname(jwt.getClaim("family_name"))
                .profilePicture(user.getProfilePicture())
                .build();
    }

    public User convertFromRequest(UserRequest userRequest) {
        return User.builder()
                .profilePicture(userRequest.profilePicture())
                .build();
    }
}
