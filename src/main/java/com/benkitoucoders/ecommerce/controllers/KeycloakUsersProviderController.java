package com.benkitoucoders.ecommerce.controllers;

import com.benkitoucoders.ecommerce.dtos.LoginResponseDto;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.dtos.SecurityUserDto;
import com.benkitoucoders.ecommerce.services.inter.SecurityUsersProviderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class KeycloakUsersProviderController {
    private final SecurityUsersProviderService securityUsersProviderService;

    @GetMapping
    public List<SecurityUserDto> getUsers(HttpServletRequest request) {
        String token = extractToken(request);
        return securityUsersProviderService.getAllUsers(token);
    }

    @GetMapping("/{username}")
    public SecurityUserDto getUserByUsername(@PathVariable String username, HttpServletRequest request) {
        String token = extractToken(request);
        return securityUsersProviderService.getUserByUsername(username, token);
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody SecurityUserDto user, HttpServletRequest request) {
        String token = extractToken(request);
        SecurityUserDto createdUser = securityUsersProviderService.addUser(user, token);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateUser(@PathVariable String id, @RequestBody SecurityUserDto user, HttpServletRequest request) {
        String token = extractToken(request);
        // Assuming the updateUser method returns the updated user
        ResponseDto updatedUser = securityUsersProviderService.updateUser(user, id, token);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteUserByUsername(@PathVariable String id, HttpServletRequest request) {
        String token = extractToken(request);
        ResponseDto response = securityUsersProviderService.deleteUserById(id, token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(HttpServletRequest request) {
        String grantType = request.getParameter("grant_type");
        String clientId = request.getParameter("client_id");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        return ResponseEntity.ok(securityUsersProviderService.login(grantType, clientId, username, password));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(HttpServletRequest request) {
        String token = extractToken(request);
        String clientId = request.getParameter("client_id");
        String refreshToken = request.getParameter("refresh_token");

        return ResponseEntity.ok(securityUsersProviderService.logout(token, clientId, refreshToken));
    }

    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new RuntimeException("No OAuth token found in the request");
    }


}
