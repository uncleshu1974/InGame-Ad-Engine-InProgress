package com.kingroly.authservice.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.security.Key;
import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utility component for generating and validating JWT Tokens.
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret:defaultSecretKeyWhichNeedsToBeLongEnoughToWorkProperly123456789}")
    private String jwtSecret;

    @Value("${jwt.expirationMs:86400000}")
    private int jwtExpirationMs; // 24 hours

    private SecretKey key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generates a JWT token based on the authenticated advertiser's email.
     */
    public String generateJwtToken(Authentication authentication) {
        String email = authentication.getName(); 
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    /**
     * Extracts the email (Subject) from the JWT.
     */
    public String getUserEmailFromJwtToken(String token) {
        return Jwts.parser().verifyWith(key()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    /**
     * Validates the signature and expiration date of the JWT.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("Invalid JWT Token: " + e.getMessage());
        }
        return false;
    }
}
