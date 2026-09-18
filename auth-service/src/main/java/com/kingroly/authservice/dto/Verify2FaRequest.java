package com.kingroly.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Verify2FaRequest {
    @NotBlank(message = "Il token temporaneo è obbligatorio")
    private String tempToken;
    
    @NotBlank(message = "Il codice OTP è obbligatorio")
    private String code;

    private String deviceInfo = "Dispositivo Sconosciuto"; // Es. "Chrome su Windows"
}
