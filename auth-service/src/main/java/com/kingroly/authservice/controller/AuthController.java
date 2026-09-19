package com.kingroly.authservice.controller;

import com.kingroly.authservice.dto.*;
import com.kingroly.authservice.model.*;
import com.kingroly.authservice.repository.*;
import com.kingroly.authservice.security.JwtUtils;
import com.kingroly.authservice.security.UserDetailsImpl;
import com.kingroly.authservice.service.EmailService;
import com.kingroly.authservice.service.PasswordResetService;
import com.kingroly.authservice.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Main REST Controller handling Authentication and Authorization.
 * 
 * DESIGN PATTERN: MVC (Model-View-Controller) -> Controller Layer.
 * This class exposes HTTP endpoints (/api/auth/*) to the outside world.
 * 
 * @RestController indicates that the data returned by each method will be written 
 * straight into the response body instead of rendering a template.
 * @CrossOrigin enables Cross-Origin Resource Sharing (CORS), allowing web frontends 
 * from different domains to communicate with these APIs.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API for managing authentication, 2FA, and advertiser sessions.")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AdvertisersRepository advertisersRepository;

    @Autowired
    OtpCodesRepository otpCodesRepository;

    @Autowired
    DeviceSessionsRepository deviceSessionsRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    EmailService emailService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    PasswordResetService passwordResetService;

    @Operation(summary = "Primary login", description = "Requires email and password. If correct, sends an email with OTP and returns a tempToken for 2FA.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful login, OTP sent"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "429", description = "Too many requests (Rate Limit exceeded)")
    })
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // Verify credentials using Spring Security's AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Advertisers advertiser = advertisersRepository.findById(userDetails.getId()).orElseThrow();

        // Generate a 6-digit OTP code for 2FA validation
        String code = String.format("%06d", new java.util.Random().nextInt(999999));
        String tempToken = UUID.randomUUID().toString();

        OtpCodes otp = new OtpCodes();
        otp.setAdvertisers(advertiser);
        otp.setTempToken(tempToken);
        otp.setCode(code);
        otp.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(5)));
        otp.setIsUsed(false);
        otpCodesRepository.save(otp);

        // Send the OTP via email (Mock implementation)
        emailService.sendOtpEmail(advertiser.getEmail(), code);

        return ResponseEntity.ok("2FA request sent. Use the tempToken: " + tempToken + " to verify access.");
    }

    @Operation(summary = "Verify 2FA OTP code", description = "Verifies the OTP and tempToken. On success, returns a JWT (Access Token) and a long-lived Refresh Token.")
    @ApiResponse(responseCode = "200", description = "Successful Verification", content = @Content(schema = @Schema(implementation = JwtResponse.class)))
    @PostMapping("/verify-2fa")
    public ResponseEntity<?> verify2fa(@Valid @RequestBody Verify2FaRequest request) {
        Optional<OtpCodes> otpOpt = otpCodesRepository.findByTempTokenAndCode(request.getTempToken(), request.getCode());

        if (otpOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid OTP Code or Temporary Token!");
        }

        OtpCodes otp = otpOpt.get();
        if (otp.getIsUsed() || otp.getExpiresAt().before(Timestamp.valueOf(LocalDateTime.now()))) {
            return ResponseEntity.badRequest().body("OTP Code expired or already used!");
        }

        otp.setIsUsed(true);
        otpCodesRepository.save(otp);

        Advertisers advertiser = otp.getAdvertisers();
        UserDetailsImpl userDetails = UserDetailsImpl.build(advertiser);

        // Create a fake security context to generate the JWT without requiring full standard login
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        // Create the device session to track logged in devices
        DeviceSessions session = new DeviceSessions();
        session.setAdvertisers(advertiser);
        session.setTokenId(jwt); // Use the entire JWT as a unique ID (or subject/jti)
        session.setDeviceInfo(request.getDeviceInfo());
        session.setIpAddress("127.0.0.1"); // In a real app, this should be extracted from the HTTP request
        session.setIsRevoked(false);
        deviceSessionsRepository.save(session);

        // Create the long-lived Refresh Token
        RefreshTokens refreshToken = refreshTokenService.createRefreshToken(advertiser.getId());

        return ResponseEntity.ok(new JwtResponse(
                jwt, 
                refreshToken.getToken(), 
                advertiser.getId(), 
                advertiser.getAdvertiserName(), 
                advertiser.getEmail()
        ));
    }

    @Operation(summary = "Register a new Advertiser", description = "Creates a new advertiser account in the Auth Service.")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (advertisersRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        Advertisers advertiser = new Advertisers();
        advertiser.setAdvertiserName(signUpRequest.getAdvertiserName());
        advertiser.setEmail(signUpRequest.getEmail());
        advertiser.setPassword(encoder.encode(signUpRequest.getPassword()));
        advertiser.setRole("ROLE_ADVERTISER");

        advertisersRepository.save(advertiser);

        return ResponseEntity.ok("Advertiser registered successfully!");
    }

    @Operation(summary = "Get active sessions", description = "Returns a list of all devices/sessions currently connected to the account. Requires Access Token (JWT).")
    @GetMapping("/sessions")
    public ResponseEntity<List<DeviceSessions>> getActiveSessions(Authentication authentication) {
        String email = authentication.getName();
        Advertisers advertiser = advertisersRepository.findByEmail(email).orElseThrow();

        List<DeviceSessions> sessions = deviceSessionsRepository.findByAdvertisers_IdAndIsRevokedFalse(advertiser.getId());
        return ResponseEntity.ok(sessions);
    }

    @Operation(summary = "Revoke an active session", description = "Manually invalidates a specific session, disconnecting the device. Requires Access Token (JWT).")
    @PostMapping("/sessions/{id}/revoke")
    public ResponseEntity<String> revokeSession(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        Advertisers advertiser = advertisersRepository.findByEmail(email).orElseThrow();

        DeviceSessions session = deviceSessionsRepository.findById(id).orElseThrow();
        
        // Verify that the session belongs to the currently logged-in user
        if (session.getAdvertisers().getId() != advertiser.getId()) {
            return ResponseEntity.status(403).body("Action not allowed");
        }

        session.setIsRevoked(true);
        deviceSessionsRepository.save(session);
        return ResponseEntity.ok("Session revoked successfully!");
    }

    @Operation(summary = "Validate JWT token", description = "Utility endpoint to quickly test if the current token (in headers) is still valid.")
    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken() {
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Renew JWT Access Token", description = "Receives a valid Refresh Token and returns a new Access Token.")
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully", content = @Content(schema = @Schema(implementation = TokenRefreshResponse.class)))
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshTokens::getAdvertisers)
                .map(advertiser -> {
                    UserDetailsImpl userDetails = UserDetailsImpl.build(advertiser);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    String token = jwtUtils.generateJwtToken(authentication);
                    
                    DeviceSessions session = new DeviceSessions();
                    session.setAdvertisers(advertiser);
                    session.setTokenId(token);
                    session.setDeviceInfo("Refreshed Session");
                    session.setIpAddress("127.0.0.1");
                    session.setIsRevoked(false);
                    deviceSessionsRepository.save(session);
                    
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @Operation(summary = "Request password recovery", description = "Generates a temporary token for password reset and sends it to the specified email.")
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        Optional<Advertisers> advertiserOpt = advertisersRepository.findByEmail(request.getEmail());
        if (advertiserOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: User not found.");
        }

        PasswordResetTokens resetToken = passwordResetService.createResetToken(advertiserOpt.get().getId());
        emailService.sendPasswordResetEmail(advertiserOpt.get().getEmail(), resetToken.getToken());

        return ResponseEntity.ok("We have sent you an email with the token to reset your password.");
    }

    @Operation(summary = "Set a new password", description = "Allows setting a new password for the account by providing the token received via email.")
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        Optional<PasswordResetTokens> tokenOpt = passwordResetService.findByToken(request.getToken());

        if (tokenOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: Invalid token.");
        }

        try {
            PasswordResetTokens validToken = passwordResetService.verifyExpiration(tokenOpt.get());
            Advertisers advertiser = validToken.getAdvertisers();
            advertiser.setPassword(encoder.encode(request.getNewPassword()));
            advertisersRepository.save(advertiser);
            
            passwordResetService.deleteToken(validToken);

            return ResponseEntity.ok("Password reset successfully! You can now log in.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(org.springframework.security.authentication.BadCredentialsException ex) {
        return ResponseEntity.status(401).body("Invalid credentials!");
    }
}
