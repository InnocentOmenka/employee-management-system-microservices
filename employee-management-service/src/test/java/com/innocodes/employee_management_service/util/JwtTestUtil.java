package com.innocodes.employee_management_service.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTestUtil {

    private static final String SECRET = "3b8b6f6a2c4a8f0b90e2c7c9f67f8e8a1d1a2b3c4d5e6f708091a1b2c3d4e5f6";

    public static String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
                .compact();
    }
}
