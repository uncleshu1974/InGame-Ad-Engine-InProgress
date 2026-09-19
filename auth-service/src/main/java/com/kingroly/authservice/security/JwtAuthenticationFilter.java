package com.kingroly.authservice.security;

import com.kingroly.authservice.model.DeviceSessions;
import com.kingroly.authservice.repository.DeviceSessionsRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * JWT Authentication Filter (Custom Spring Security Filter).
 * 
 * DESIGN PATTERN: Intercepting Filter / API Gateway concept.
 * This filter intercepts all incoming HTTP requests BEFORE they reach the REST Controllers.
 * It extracts the JWT from the "Authorization" header, validates its cryptographic signature,
 * and checks its expiration date using JwtUtils.
 * 
 * If valid, it constructs a UsernamePasswordAuthenticationToken and places it into the 
 * SecurityContextHolder, effectively telling Spring Security: "This user is authenticated."
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private DeviceSessionsRepository deviceSessionsRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Step 1: Extract JWT string from HTTP header
            String jwt = parseJwt(request);
            
            // Step 2: Validate the JWT cryptographic signature and expiration
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                
                // EXTRA CHECK: Verify if the token was revoked in the DB (Device Dashboard)
                Optional<DeviceSessions> sessionOpt = deviceSessionsRepository.findByTokenId(jwt);
                if (sessionOpt.isPresent() && !sessionOpt.get().getIsRevoked()) {
                    
                    String email = jwtUtils.getUserEmailFromJwtToken(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    
                    // Create the authentication token and attach it to the SecurityContext
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    System.err.println("Login attempt with revoked or non-existent Token!");
                }
            }
        } catch (Exception e) {
            System.err.println("Cannot set user authentication: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
