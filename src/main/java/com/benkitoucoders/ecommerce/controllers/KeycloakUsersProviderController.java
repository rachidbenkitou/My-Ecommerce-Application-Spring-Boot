package com.benkitoucoders.ecommerce.controllers;

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
        ResponseDto updatedUser = securityUsersProviderService.updateUser(user,id, token);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteUserByUsername(@PathVariable String id, HttpServletRequest request) {
        String token = extractToken(request);
        ResponseDto response = securityUsersProviderService.deleteUserById(id, token);
        return ResponseEntity.ok(response);
    }

    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new RuntimeException("No OAuth token found in the request");
    }


}
