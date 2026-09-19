package com.kingroly.campaignservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Swagger/OpenAPI documentation.
 * 
 * DESIGN PATTERN: Configuration Class.
 * This class sets up the global API documentation. 
 * The @SecurityScheme configures the "Authorize" button in the Swagger UI,
 * allowing developers to inject a JWT Bearer token into all subsequent requests for testing.
 */
/**
 * Configurazione Globale di Swagger e OpenAPI per l'Ad Campaign Service.
 * 
 * DESIGN PATTERN: Configuration Class.
 * Questo file permette di generare la documentazione interattiva dell'API (visibile su /swagger-ui.html).
 * La sua funzione principale è istruire Swagger su come gestire la sicurezza per le chiamate REST.
 */
@Configuration
/**
 * @OpenAPIDefinition fornisce le informazioni di base (titolo, descrizione) mostrate nell'header di Swagger.
 */
@OpenAPIDefinition(info = @Info(title = "Ad Campaign Service API", version = "v1", description = "API documentation for the Ad Campaign Service"))
/**
 * @SecurityScheme configura il meccanismo di autenticazione per l'interfaccia Swagger.
 * Impostando lo schema "bearer" e il formato "JWT", Swagger creerà il pulsante "Authorize".
 * In questo modo, il front-end (o te che testi manualmente) può inserire il JWT generato 
 * dall'Auth Service, e Swagger lo aggiungerà in automatico come "Bearer Token" a ogni chiamata HTTP.
 */
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApiConfig {
}
