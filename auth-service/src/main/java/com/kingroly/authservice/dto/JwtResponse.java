package com.kingroly.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for the JWT response payload.
 * 
 * This object is serialized into JSON and returned to the client upon successful 2FA verification.
 * It contains the generated JWT ('token') and basic user profile information.
 */
@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String advertiserName;
    private String email;

    public JwtResponse(String token, String refreshToken, Long id, String advertiserName, String email) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.id = id;
        this.advertiserName = advertiserName;
        this.email = email;
    }
}
