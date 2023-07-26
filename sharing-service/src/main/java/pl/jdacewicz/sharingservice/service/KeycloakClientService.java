package pl.jdacewicz.sharingservice.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;

import java.util.List;

@Service
public class KeycloakClientService {

    @Value("${spring.security.oauth2.resourceserver.keycloak.realm}")
    private String realm;

    @Value("${message.user.not-found}")
    private String notFoundMessage;

    private final Keycloak keycloak;

    @Autowired
    public KeycloakClientService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public UserRepresentation getUserByEmail(String email) {
        List<UserRepresentation> users =  keycloak.realm(realm)
                .users()
                .searchByEmail(email, true);

        if (users.isEmpty()) {
            throw new RecordNotFoundException(notFoundMessage);
        }
        return users.get(0);
    }
}
