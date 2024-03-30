package com.benkitoucoders.ecommerce.utils;

import jakarta.servlet.http.HttpServletRequest;

public class TokenManagement {
    public static String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new RuntimeException("No OAuth token found in the request");
    }
}
