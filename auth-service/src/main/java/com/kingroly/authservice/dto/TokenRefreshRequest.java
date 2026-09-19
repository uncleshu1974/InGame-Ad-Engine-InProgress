package com.kingroly.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for requesting a new JWT via a Refresh Token.
 * 
 * The client sends its long-lived Refresh Token to this endpoint to obtain a fresh 
 * short-lived JWT once the previous one expires.
 */
@Data
public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;
}
