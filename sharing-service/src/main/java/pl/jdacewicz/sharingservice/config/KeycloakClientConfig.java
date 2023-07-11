package pl.jdacewicz.sharingservice.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakClientConfig {

    @Value("${spring.security.oauth2.resourceserver.keycloak.server-url}")
    private String serverUrl;

    @Value("${spring.security.oauth2.resourceserver.keycloak.realm}")
    private String realm;

    @Value("${spring.security.oauth2.resourceserver.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.keycloak.admin.username}")
    private String username;

    @Value("${spring.security.oauth2.resourceserver.keycloak.admin.password}")
    private String password;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .grantType(OAuth2Constants.PASSWORD)
                .username(username)
                .password(password)
                .build();
    }
}
