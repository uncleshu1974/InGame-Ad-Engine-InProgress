package com.kingroly.authservice.security;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        // Inietta i valori simulati per il secret e la scadenza
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "ThisIsAVerySecureAndLongSecretKeyForTestingPurposesOnly1234567890!");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000); // 1 ora
    }

    @Test
    void testGenerateJwtToken() {
        // Arrange
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("test@advertiser.com");

        // Act
        String token = jwtUtils.generateJwtToken(authentication);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGetUserEmailFromJwtToken() {
        // Arrange
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("test@advertiser.com");
        String token = jwtUtils.generateJwtToken(authentication);

        // Act
        String email = jwtUtils.getUserEmailFromJwtToken(token);

        // Assert
        assertEquals("test@advertiser.com", email);
    }

    @Test
    void testValidateJwtToken_ValidToken() {
        // Arrange
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("test@advertiser.com");
        String token = jwtUtils.generateJwtToken(authentication);

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testValidateJwtToken_InvalidToken() {
        // Act
        boolean isValid = jwtUtils.validateJwtToken("invalid.token.here");

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_ExpiredToken() {
        // Arrange
        // Settiamo un'espirazione nel passato (es. 1 ms)
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 1);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("test@advertiser.com");
        String token = jwtUtils.generateJwtToken(authentication);

        // Aspettiamo che il token scada
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertFalse(isValid);
    }
}
