package com.benkitoucoders.ecommerce.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {
    @Bean
    public Keycloak keycloak() {


        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080/realms/master")
                .realm("master")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId("admin-cli")
                .clientSecret("FJRMxrU81GGOhA4KlPHPLbOcSbbSaxPD")
                .build();
    }
}
