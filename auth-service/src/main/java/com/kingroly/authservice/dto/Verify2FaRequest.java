package com.kingroly.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for Two-Factor Authentication (2FA) verification requests.
 * 
 * Captures the temporary token (received after step 1 of login) and the 6-digit OTP code 
 * sent to the user's email.
 */
@Data
public class Verify2FaRequest {
    @NotBlank(message = "Temporary token is required")
    private String tempToken;
    
    @NotBlank(message = "OTP code is required")
    private String code;

    private String deviceInfo = "Unknown Device"; // E.g. "Chrome on Windows"
}
