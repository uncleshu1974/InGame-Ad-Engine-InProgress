package com.kingroly.authservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configurazione Globale di Swagger e OpenAPI.
 * 
 * DESIGN PATTERN: Configuration Class.
 * Questo file è fondamentale per generare in automatico la documentazione dell'API (l'interfaccia grafica
 * che vedi all'indirizzo /swagger-ui.html). Senza questo file, Swagger non saprebbe come descrivere
 * l'applicazione o come gestire l'autenticazione tramite JWT.
 */
@Configuration
/**
 * @OpenAPIDefinition definisce le informazioni base del progetto (titolo, versione, descrizione).
 * Questi sono i dati che appaiono in cima alla pagina di Swagger UI.
 */
@OpenAPIDefinition(info = @Info(title = "Auth Service API", version = "v1", description = "API documentation for the Authentication Service"))
/**
 * @SecurityScheme istruisce Swagger su come gestire l'autenticazione.
 * Dicendogli che il tipo è HTTP, il formato è JWT e lo schema è "bearer", Swagger fa comparire
 * il pulsante "Authorize" verde in alto a destra. Quando inserisci il token lì dentro, 
 * Swagger saprà che dovrà automaticamente aggiungere l'header "Authorization: Bearer <token>"
 * a tutte le richieste verso endpoint protetti.
 */
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApiConfig {
}
