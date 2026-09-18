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
 * Componente per la generazione e validazione dei Token JWT.
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret:defaultSecretKeyWhichNeedsToBeLongEnoughToWorkProperly123456789}")
    private String jwtSecret;

    @Value("${jwt.expirationMs:86400000}")
    private int jwtExpirationMs; // 24 ore

    private SecretKey key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Genera un token JWT basato sull'email dell'inserzionista autenticato.
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
     * Estrae l'email (Subject) dal JWT.
     */
    public String getUserEmailFromJwtToken(String token) {
        return Jwts.parser().verifyWith(key()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    /**
     * Valida la firma e la scadenza del JWT.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("Token JWT Non Valido: " + e.getMessage());
        }
        return false;
    }
}
