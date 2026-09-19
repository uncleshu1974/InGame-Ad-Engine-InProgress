package com.kingroly.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for the payload returned after a successful token refresh.
 * 
 * @AllArgsConstructor (Lombok) automatically generates a constructor with 1 parameter for each field in the class.
 */
@Data
@AllArgsConstructor
public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public TokenRefreshResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
