package com.kingroly.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for securely submitting a new password.
 * 
 * It requires the unique token sent to the user's email along with the new password string.
 */
@Data
public class PasswordResetRequest {
    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;
}
