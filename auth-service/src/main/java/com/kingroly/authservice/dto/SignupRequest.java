package com.kingroly.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for advertiser registration requests.
 * 
 * DESIGN PATTERN: The DTO pattern is used to decouple the API payload from the internal 
 * Database Entities. It ensures that clients only send/receive the exact fields required.
 * 
 * @Data (Lombok) automatically generates getters, setters, toString, equals, and hashCode methods at compile time,
 * significantly reducing boilerplate code.
 */
@Data
public class SignupRequest {
    /**
     * @NotBlank ensures the field is not null and not empty (it trims whitespace).
     * The 'message' parameter is returned to the client if validation fails (e.g., HTTP 400 Bad Request).
     */
    @NotBlank(message = "Advertiser name is required")
    private String advertiserName;

    /**
     * @Email ensures the provided string has a valid email format (e.g., containing '@' and a domain).
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
