package com.benkitoucoders.ecommerce.controllers;

import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.entities.Role;
import com.benkitoucoders.ecommerce.services.inter.SecurityRolesProviderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("roles")
public class KeycloakRolesProviderController {
    private final SecurityRolesProviderService securityRolesProviderService;

    @GetMapping
    public List<Role> getAllRoles(HttpServletRequest request) {
        String token = extractToken(request);
        return securityRolesProviderService.getAllRoles(token);
    }

    @GetMapping("/{name}")
    public Role getRoleById(@PathVariable String name, HttpServletRequest request) {
        String token = extractToken(request);
        return securityRolesProviderService.getRoleByName(name, token);
    }

    @PostMapping
    public ResponseEntity<Role> addRole(@RequestBody Role role, HttpServletRequest request) {
        String token = extractToken(request);
        Role createdRole = securityRolesProviderService.addRole(role, token);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<ResponseDto> deleteRoleById(@PathVariable String name, HttpServletRequest request) {
        String token = extractToken(request);
        ResponseDto response = securityRolesProviderService.deleteRoleByName(name, token);
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
