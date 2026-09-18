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
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // Verifica credenziali tramite Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Advertisers advertiser = advertisersRepository.findById(userDetails.getId()).orElseThrow();

        // Generazione OTP a 6 cifre
        String code = String.format("%06d", new java.util.Random().nextInt(999999));
        String tempToken = UUID.randomUUID().toString();

        OtpCodes otp = new OtpCodes();
        otp.setAdvertisers(advertiser);
        otp.setTempToken(tempToken);
        otp.setCode(code);
        otp.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(5)));
        otp.setIsUsed(false);
        otpCodesRepository.save(otp);

        // Invia l'email (Mock)
        emailService.sendOtpEmail(advertiser.getEmail(), code);

        return ResponseEntity.ok("Richiesta 2FA inviata. Usa il tempToken: " + tempToken + " per verificare l'accesso.");
    }

    @Operation(summary = "Verify 2FA OTP code", description = "Verifies the OTP and tempToken. On success, returns a JWT (Access Token) and a long-lived Refresh Token.")
    @PostMapping("/verify-2fa")
    public ResponseEntity<?> verify2fa(@Valid @RequestBody Verify2FaRequest request) {
        Optional<OtpCodes> otpOpt = otpCodesRepository.findByTempTokenAndCode(request.getTempToken(), request.getCode());

        if (otpOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Codice OTP o Token Temporaneo non valido!");
        }

        OtpCodes otp = otpOpt.get();
        if (otp.getIsUsed() || otp.getExpiresAt().before(Timestamp.valueOf(LocalDateTime.now()))) {
            return ResponseEntity.badRequest().body("Codice OTP scaduto o già utilizzato!");
        }

        otp.setIsUsed(true);
        otpCodesRepository.save(otp);

        Advertisers advertiser = otp.getAdvertisers();
        UserDetailsImpl userDetails = UserDetailsImpl.build(advertiser);

        // Creiamo il contesto di sicurezza fittizio per generare il token
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        // Crea la sessione del dispositivo
        DeviceSessions session = new DeviceSessions();
        session.setAdvertisers(advertiser);
        session.setTokenId(jwt); // Usiamo l'intero JWT come ID univoco (o il subject/jti)
        session.setDeviceInfo(request.getDeviceInfo());
        session.setIpAddress("127.0.0.1"); // In un'app reale si prende dalla request HTTP
        session.setIsRevoked(false);
        deviceSessionsRepository.save(session);

        // Crea il Refresh Token
        RefreshTokens refreshToken = refreshTokenService.createRefreshToken(advertiser.getId());

        return ResponseEntity.ok(new TokenRefreshResponse(jwt, refreshToken.getToken()));
    }

    @Operation(summary = "Register a new Advertiser", description = "Creates a new advertiser account in the Auth Service.")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (advertisersRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Errore: L'email è già in uso!");
        }

        Advertisers advertiser = new Advertisers();
        advertiser.setCompanyName(signUpRequest.getCompanyName());
        advertiser.setEmail(signUpRequest.getEmail());
        advertiser.setPassword(encoder.encode(signUpRequest.getPassword()));
        advertiser.setRole("ROLE_ADVERTISER");

        advertisersRepository.save(advertiser);

        return ResponseEntity.ok("Inserzionista registrato con successo!");
    }

    @Operation(summary = "Get active sessions", description = "Returns a list of all devices/sessions currently connected to the account. Requires Access Token (JWT).")
    @GetMapping("/sessions")
    public ResponseEntity<?> getActiveSessions(Authentication authentication) {
        String email = authentication.getName();
        Advertisers advertiser = advertisersRepository.findByEmail(email).orElseThrow();

        List<DeviceSessions> sessions = deviceSessionsRepository.findByAdvertisers_IdAndIsRevokedFalse(advertiser.getId());
        return ResponseEntity.ok(sessions);
    }

    @Operation(summary = "Revoke an active session", description = "Manually invalidates a specific session, disconnecting the device. Requires Access Token (JWT).")
    @PostMapping("/sessions/{id}/revoke")
    public ResponseEntity<?> revokeSession(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        Advertisers advertiser = advertisersRepository.findByEmail(email).orElseThrow();

        DeviceSessions session = deviceSessionsRepository.findById(id).orElseThrow();
        
        // Verifica che la sessione appartenga all'utente loggato
        if (session.getAdvertisers().getId() != advertiser.getId()) {
            return ResponseEntity.status(403).body("Azione non consentita");
        }

        session.setIsRevoked(true);
        deviceSessionsRepository.save(session);
        return ResponseEntity.ok("Sessione revocata con successo!");
    }

    @Operation(summary = "Validate JWT token", description = "Utility endpoint to quickly test if the current token (in headers) is still valid.")
    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken() {
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Renew JWT Access Token", description = "Receives a valid Refresh Token and returns a new Access Token.")
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
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @Operation(summary = "Request password recovery", description = "Generates a temporary token for password reset and sends it to the specified email.")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        Optional<Advertisers> advertiserOpt = advertisersRepository.findByEmail(request.getEmail());
        if (advertiserOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Errore: Utente non trovato.");
        }

        PasswordResetTokens resetToken = passwordResetService.createResetToken(advertiserOpt.get().getId());
        emailService.sendPasswordResetEmail(advertiserOpt.get().getEmail(), resetToken.getToken());

        return ResponseEntity.ok("Ti abbiamo inviato un'email con il token per reimpostare la password.");
    }

    @Operation(summary = "Set a new password", description = "Allows setting a new password for the account by providing the token received via email.")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        Optional<PasswordResetTokens> tokenOpt = passwordResetService.findByToken(request.getToken());

        if (tokenOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Errore: Token invalido.");
        }

        try {
            PasswordResetTokens validToken = passwordResetService.verifyExpiration(tokenOpt.get());
            Advertisers advertiser = validToken.getAdvertisers();
            advertiser.setPassword(encoder.encode(request.getNewPassword()));
            advertisersRepository.save(advertiser);
            
            passwordResetService.deleteToken(validToken);

            return ResponseEntity.ok("Password reimpostata con successo! Ora puoi effettuare il login.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
