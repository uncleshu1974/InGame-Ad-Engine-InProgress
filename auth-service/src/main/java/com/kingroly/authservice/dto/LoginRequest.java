package com.kingroly.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for login requests.
 * 
 * Used to capture the credentials sent by the client. Validation annotations like @NotBlank 
 * and @Email ensure that the controller only processes valid formats, offloading validation logic 
 * from the service layer.
 */
@Data
public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
