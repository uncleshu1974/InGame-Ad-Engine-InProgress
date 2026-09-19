package com.kingroly.authservice.service;

import com.kingroly.authservice.model.Advertisers;
import com.kingroly.authservice.model.PasswordResetTokens;
import com.kingroly.authservice.repository.AdvertisersRepository;
import com.kingroly.authservice.repository.PasswordResetTokensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Service handling the business logic for Password Resets.
 * 
 * DESIGN PATTERN: Service Layer.
 * This class coordinates between the Advertisers and PasswordResetTokens repositories
 * to generate secure, time-limited reset tokens (UUIDs) and validate them later.
 */
@Service
public class PasswordResetService {

    private final Long resetTokenDurationMs = 900000L; // 15 minutes

    @Autowired
    private PasswordResetTokensRepository passwordResetTokensRepository;

    @Autowired
    private AdvertisersRepository advertisersRepository;

    /**
     * Creates a new unique reset token for a given advertiser ID.
     * The token is set to expire 15 minutes from creation.
     */
    public PasswordResetTokens createResetToken(Long advertiserId) {
        PasswordResetTokens token = new PasswordResetTokens();

        Optional<Advertisers> opt = advertisersRepository.findById(advertiserId);
        if (opt.isEmpty()) return null;

        token.setAdvertisers(opt.get());
        token.setExpiresAt(Timestamp.from(Instant.now().plusMillis(resetTokenDurationMs)));
        token.setToken(UUID.randomUUID().toString());

        token = passwordResetTokensRepository.save(token);
        return token;
    }

    /**
     * Verifies if a given token has expired based on its 'expiresAt' timestamp.
     * If expired, it automatically deletes the token from the DB and throws an Exception.
     */
    public PasswordResetTokens verifyExpiration(PasswordResetTokens token) {
        if (token.getExpiresAt().before(Timestamp.from(Instant.now()))) {
            passwordResetTokensRepository.delete(token);
            throw new RuntimeException("Reset token is expired. Please make a new request");
        }
        return token;
    }

    public Optional<PasswordResetTokens> findByToken(String token) {
        return passwordResetTokensRepository.findByToken(token);
    }
    
    public void deleteToken(PasswordResetTokens token) {
        passwordResetTokensRepository.delete(token);
    }
}
