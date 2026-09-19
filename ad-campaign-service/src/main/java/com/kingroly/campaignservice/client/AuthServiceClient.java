package com.kingroly.campaignservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import org.springframework.web.bind.annotation.RequestHeader;

/**
 * OpenFeign Client for synchronous microservice-to-microservice communication.
 * 
 * DESIGN PATTERN: API Gateway / Remote Facade.
 * Instead of writing boilerplate RestTemplate code, Feign allows us to define 
 * HTTP clients declaratively via interfaces.
 * 
 * @FeignClient automatically generates the proxy implementation at runtime. 
 * 'name' is the logical name of the target service, and 'url' points to its location.
 */
@FeignClient(name = "auth-service", url = "http://localhost:8084")
public interface AuthServiceClient {

    /**
     * Calls the /api/auth/validate-token endpoint on the auth-service.
     * Passes the JWT token in the Authorization header to verify its validity 
     * (and check if it has been revoked in the DB).
     */
    @GetMapping("/api/auth/validate-token")
    boolean validateToken(@RequestHeader("Authorization") String token);
}
