package com.eg.yatc.core.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private String secretKey = "a-very-long-secret-key-that-is-at-least-32-bytes!!";
    private SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

    public JwtUtil() {
    }

//    // Generate token
//    public String generateToken(CustomUserDetails customUserDetails) {
//
//        //return JwtBuilder.
//        return Jwts.builder()
//                .setSubject(customUserDetails.getUsername())
//                .claim("role", customUserDetails.getAuthorities())
//                .claim("userId", customUserDetails.getUserId())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
//                .signWith(key)
//                .compact();
//    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extract username from token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Validate token
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

//    // Extract claims (subject, expiration, etc.)
//    private Claims extractClaims(String token) {
//        JwtParser parser = Jwts.parser() // Updated for new version
//                .setSigningKey(key) // Set the secret key for signing
//                .build(); // Build the parser
//
//        return parser.parseClaimsJws(token).getBody(); // Parse the claims
//    }
}
