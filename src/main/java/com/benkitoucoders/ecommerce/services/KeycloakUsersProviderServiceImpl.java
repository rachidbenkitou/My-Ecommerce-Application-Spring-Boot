package com.benkitoucoders.ecommerce.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.benkitoucoders.ecommerce.dtos.LoginResponseDto;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.dtos.SecurityUserDto;
import com.benkitoucoders.ecommerce.dtos.UserPasswordDto;
import com.benkitoucoders.ecommerce.entities.Role;
import com.benkitoucoders.ecommerce.exceptions.EntityAlreadyExistsException;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.benkitoucoders.ecommerce.services.inter.SecurityRolesProviderService;
import com.benkitoucoders.ecommerce.services.inter.SecurityUsersProviderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class KeycloakUsersProviderServiceImpl implements SecurityUsersProviderService {
    @Value("${myKeycloak.users-endpoint}")
    private String usersEndpoint;
    @Value("${myKeycloak.token-endpoint}")
    private String loginEndpoint;
    @Value("${myKeycloak.logout-endpoint}")
    private String logoutEndpoint;

    private final RestTemplate restTemplate;
    private final SecurityRolesProviderService securityRolesProviderService;

    private final Keycloak keycloak;

    KeycloakUsersProviderServiceImpl(Keycloak keycloak, RestTemplate restTemplate, SecurityRolesProviderService securityRolesProviderService) {
        this.keycloak = keycloak;
        this.restTemplate = restTemplate;
        this.securityRolesProviderService = securityRolesProviderService;
    }


    @Override
    public List<SecurityUserDto> getAllUsers(String accessToken) {
        ResponseEntity<SecurityUserDto[]> response = makeKeycloakRequest(usersEndpoint, HttpMethod.GET, accessToken, null, SecurityUserDto[].class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return Arrays.asList(response.getBody());
        } else {
            throw new RuntimeException("Failed to fetch users from Keycloak");
        }
    }

    @Override
    public SecurityUserDto getUserByUsername(String username, String token) {
        ResponseEntity<SecurityUserDto[]> response = makeKeycloakRequest(usersEndpoint + "?username=" + username, HttpMethod.GET, token, null, SecurityUserDto[].class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().length > 0) {
            return response.getBody()[0];
        } else {
            throw new EntityNotFoundException("User not found: " + username);
        }
    }

    public SecurityUserDto getUserByUsernameWithoutException(String username, String token) {
        ResponseEntity<SecurityUserDto[]> response = makeKeycloakRequest(usersEndpoint + "?username=" + username, HttpMethod.GET, token, null, SecurityUserDto[].class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().length > 0) {
            return response.getBody()[0];
        } else {
            return null;
        }
    }

    @Override
    public SecurityUserDto getUserById(String id, String token) {
        ResponseEntity<SecurityUserDto[]> response = makeKeycloakRequest(usersEndpoint + "?id=" + id, HttpMethod.GET, token, null, SecurityUserDto[].class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().length > 0) {
            return response.getBody()[0];
        } else {
            throw new EntityNotFoundException("User not found: " + id);
        }
    }

    @Override
    public void setUpPasswordToUser(UserPasswordDto userPasswordDto, String userId, String token) {
        try {
            isUserExistsByUsername(token);
            ResponseEntity<UserPasswordDto> response = makeKeycloakRequest(usersEndpoint + "/" + userId + "/reset-password", HttpMethod.PUT, token, userPasswordDto, UserPasswordDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Error while affecting new password to user " + userId);
        }
    }

    @Override
    public SecurityUserDto addUser(SecurityUserDto user, String token) {

        isUserExistsByUsername(token);
        String userPassword = user.getPassword();
        user.setPassword(null);
        ResponseEntity<SecurityUserDto> response = makeKeycloakRequest(usersEndpoint, HttpMethod.POST, token, user, SecurityUserDto.class);
        if (response.getStatusCode() == HttpStatus.CREATED) {
            SecurityUserDto savedSecurityUserDto = getUserByUsername(user.getUsername(), token);

            setUpPasswordToUser(
                    UserPasswordDto.builder()
                            .temporary(false)
                            .type("password")
                            .value(userPassword)
                            .build()
                    , savedSecurityUserDto.getId()
                    , token);
            return savedSecurityUserDto;
        } else {
            throw new RuntimeException("Failed to create user in Keycloak");
        }
    }

    @Override
    public ResponseDto updateUser(SecurityUserDto user, String id, String token) {
        if (getUserById(id, token) == null) {
            throw new EntityNotFoundException("The user with the id" + id + "is not found");
        }
        ResponseEntity<SecurityUserDto> response = makeKeycloakRequest(usersEndpoint + "/" + id, HttpMethod.PUT, token, user, SecurityUserDto.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return ResponseDto.builder()
                    .message("User updated successfully.")
                    .build();
        } else {
            throw new RuntimeException("Failed to update user in Keycloak");
        }
    }

    @Override
    public ResponseDto deleteUserById(String id, String token) {
        isUserExistsByUsername(token);
        ResponseEntity<Void> response = makeKeycloakRequest(usersEndpoint + "/" + id, HttpMethod.DELETE, token, null, Void.class);
        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return ResponseDto.builder()
                    .message("User deleted successfully.")
                    .build();
        } else {
            throw new RuntimeException("Failed to delete user in Keycloak");
        }
    }

    // Common method to make HTTP requests
    private ResponseEntity<Map<String, Object>> makeHttpRequest(String endpoint, HttpMethod method, MultiValueMap<String, String> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(endpoint, method, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {
        });
    }

    @Override
    public LoginResponseDto login(String grantType, String clientId, String clientSecret, String username, String password) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", grantType);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("username", username);
        requestBody.add("password", password);

        ResponseEntity<Map<String, Object>> response = makeHttpRequest(loginEndpoint, HttpMethod.POST, requestBody);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            return LoginResponseDto.builder()
                    .accessToken(responseBody.get("access_token") + "")
                    .refreshToken(responseBody.get("refresh_token") + "")
                    .build();
        } else {
            throw new RuntimeException("Failed to login to Keycloak, status code: " + response.getStatusCode());
        }
    }

    @Override
    public ResponseDto logout(String token, String clientId, String refreshToken, String clientSecret) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("refresh_token", refreshToken);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);

        ResponseEntity<Map<String, Object>> response = makeHttpRequest(logoutEndpoint, HttpMethod.POST, requestBody);

        if (response.getStatusCode() != HttpStatus.NO_CONTENT) {
            throw new RuntimeException("Failed to logout from Keycloak");
        } else {

            return ResponseDto.builder()
                    .message("Logout has been successful!")
                    .build();
        }
    }

    @Override
    public ResponseDto assignRoleToUser(String userId, List<Role> roles, String token) {
        String url = usersEndpoint + "/" + userId + "/role-mappings/realm";

        // Those two line are used to verify if user and roles exist, else exception will be thrown
        getUserById(userId, token);
        for (Role role : roles) {
            securityRolesProviderService.getRoleByName(role.getName(), token);
        }
        // Convert roles to JSON array
        ObjectMapper objectMapper = new ObjectMapper();
        String rolesJson;
        try {
            rolesJson = objectMapper.writeValueAsString(roles);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert roles to JSON: " + e.getMessage());
        }

        // Make HTTP request to assign roles to user
        ResponseEntity<Void> response = makeKeycloakRequest(url, HttpMethod.POST, token, rolesJson, Void.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return ResponseDto.builder()
                    .message("Roles assigned to user successfully.")
                    .build();
        } else {
            throw new RuntimeException("Failed to assign roles to user in Keycloak");
        }
    }


    private <T, R> ResponseEntity<T> makeKeycloakRequest(String url, HttpMethod method, String token, R body, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<R> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(url, method, entity, responseType);
    }


    //Helper Method that verifies if user exits with username
    private void isUserExistsByUsername(String token) {
        String username = extractPreferredUsername(token);
        SecurityUserDto existingUser = getUserByUsernameWithoutException(username, token);
        if (existingUser != null) {
            // If we found a user, then the user already exists
            throw new EntityAlreadyExistsException("User already exists with username: " + username);
        }
    }

    private String extractPreferredUsername(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7); // Remove 'Bearer ' prefix
            try {
                DecodedJWT decodedJWT = JWT.decode(jwtToken);
                return decodedJWT.getClaim("preferred_username").asString();
            } catch (Exception e) {
                // Handle exception (e.g., token parsing error)
                // Log this error or handle it according to your application's needs
                System.err.println("Error decoding JWT: " + e.getMessage());
            }
        }
        return null; // Return null or consider throwing an exception if preferred_username cannot be extracted
    }

    @Override
    public UsersResource getUsersResource() {
        RealmResource realm1 = keycloak.realm("master");
        return realm1.users();
    }

    @Override
    public void forgotPassword(String username) {

        UsersResource usersResource = getUsersResource();

        List<UserRepresentation> representationList = usersResource.searchByUsername(username, true);
        UserRepresentation userRepresentation = representationList.stream().findFirst().orElse(null);


        if (userRepresentation != null) {

            List<String> actions = new ArrayList<>();
            actions.add("UPDATE_PASSWORD");

            usersResource.get(userRepresentation.getId()).executeActionsEmail(actions);
            return;
        }

        throw new RuntimeException("Username Not Found");
    }

}
