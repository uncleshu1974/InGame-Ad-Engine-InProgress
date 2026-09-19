package com.kingroly.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object (DTO) used when an advertiser requests a password reset link.
 */
@Data
public class ForgotPasswordRequest {
    @NotBlank
    @Email
    private String email;
}
