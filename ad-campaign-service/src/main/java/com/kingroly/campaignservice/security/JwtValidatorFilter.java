package com.kingroly.campaignservice.security;

import com.kingroly.campaignservice.client.AuthServiceClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Validator Filter.
 * 
 * DESIGN PATTERN: API Gateway pattern / Intercepting Filter.
 * Since ad-campaign-service is a resource server without a database connection to AuthDB,
 * it cannot independently verify if a token was revoked. 
 * Therefore, it intercepts incoming requests and makes a synchronous call to the 
 * auth-service via OpenFeign to validate the token.
 */
@Component
public class JwtValidatorFilter extends OncePerRequestFilter {

    @Autowired
    private AuthServiceClient authServiceClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            try {
                // Synchronous call via OpenFeign to the auth-service
                boolean isValid = authServiceClient.validateToken(headerAuth);
                if (isValid) {
                    // Valid token (not revoked), proceed with the filter chain
                    filterChain.doFilter(request, response);
                    return;
                }
            } catch (Exception e) {
                // If Feign throws an exception (e.g., 401 Unauthorized returned by auth-service)
                System.out.println("Validation failed: " + e.getMessage());
            }
        }

        // If we reach this point, the token is missing, malformed, or revoked
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Access Denied: Invalid or revoked token");
    }
}
