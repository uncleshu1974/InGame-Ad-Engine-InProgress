package com.kingroly.authservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main Spring Security Configuration Class.
 * 
 * DESIGN PATTERN: Security Configuration / Facade.
 * This class dictates how the application handles authentication, authorization, and session management.
 * @Configuration tells Spring this is a configuration bean.
 * @EnableWebSecurity enables Spring Security's web security support.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * The SecurityFilterChain defines the security rules for HTTP requests.
     * 
     * - CSRF is disabled because we use JWTs (Stateless architecture), which are immune to CSRF if stored properly.
     * - SessionCreationPolicy.STATELESS tells Spring Security NEVER to create an HttpSession. 
     *   Every request must be authenticated via the JWT.
     * - permitAll() allows open access to login, registration, and swagger UI endpoints.
     * - anyRequest().authenticated() forces all other endpoints to require a valid token.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> 
                auth.requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/verify-2fa", "/error", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/api/auth/refreshtoken", "/api/auth/forgot-password", "/api/auth/reset-password").permitAll() 
                .anyRequest().authenticated() 
            );
        
        // Add our custom JWT filter BEFORE the standard UsernamePasswordAuthenticationFilter
        // so it can authenticate requests using the token before Spring tries to use a session/form login.
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
