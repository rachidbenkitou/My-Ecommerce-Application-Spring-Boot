//package com.benkitoucoders.ecommerce.services;
//
//import com.benkitoucoders.ecommerce.dtos.ResponseDto;
//import com.benkitoucoders.ecommerce.entities.Role;
//import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
//import com.benkitoucoders.ecommerce.services.inter.SecurityRolesProviderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Transactional
//@Service
//@RequiredArgsConstructor
//public class KeycloakRolesProviderServiceImpl implements SecurityRolesProviderService {
//    @Value("${myKeycloak.roles-endpoint}")
//    private String rolesEndpoint;
//
//    private final RestTemplate restTemplate;
//
//    @Override
//    public List<Role> getAllRoles(String accessToken) {
//        ResponseEntity<Role[]> response = makeKeycloakRequest(rolesEndpoint, HttpMethod.GET, accessToken, null, Role[].class);
//        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//            return Arrays.asList(response.getBody());
//        } else {
//            throw new RuntimeException("Failed to fetch roles from Keycloak");
//        }
//    }
//
//    @Override
//    public Role getRoleByName(String name, String token) {
//        ResponseEntity<Role> response = makeKeycloakRequest(rolesEndpoint + "/" + name, HttpMethod.GET, token, null, Role.class);
//        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//            return response.getBody();
//        } else {
//            throw new EntityNotFoundException("Role not found: " + name);
//        }
//    }
//
//    @Override
//    public Role addRole(Role role, String token) {
//        ResponseEntity<Role> response = makeKeycloakRequest(rolesEndpoint, HttpMethod.POST, token, role, Role.class);
//        if (response.getStatusCode() == HttpStatus.CREATED) {
//            return getRoleByName(role.getName(), token);
//        } else {
//            throw new RuntimeException("Failed to create role in Keycloak");
//        }
//    }
//
//    @Override
//    public ResponseDto deleteRoleByName(String name, String token) {
//        ResponseEntity<Void> response = makeKeycloakRequest(rolesEndpoint + "/" + name, HttpMethod.DELETE, token, null, Void.class);
//        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
//            return ResponseDto.builder()
//                    .message("Role deleted successfully.")
//                    .build();
//        } else {
//            throw new RuntimeException("Failed to delete role in Keycloak");
//        }
//    }
//
//    // Common method to make HTTP requests
//    private <T, R> ResponseEntity<T> makeKeycloakRequest(String url, HttpMethod method, String token, R body, Class<T> responseType) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<R> entity = new HttpEntity<>(body, headers);
//
//        return restTemplate.exchange(url, method, entity, responseType);
//    }
//}
