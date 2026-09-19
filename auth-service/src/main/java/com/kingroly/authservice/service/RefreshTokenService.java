package com.kingroly.authservice.service;

import com.kingroly.authservice.model.Advertisers;
import com.kingroly.authservice.model.RefreshTokens;
import com.kingroly.authservice.repository.AdvertisersRepository;
import com.kingroly.authservice.repository.RefreshTokensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for handling long-lived Refresh Tokens.
 * 
 * DESIGN PATTERN: Service Layer.
 * Manages token generation, expiration verification, and database persistence.
 * Refresh Tokens are used to maintain user sessions without forcing frequent manual logins.
 */
@Service
public class RefreshTokenService {

    private final Long refreshTokenDurationMs = 604800000L; // 7 days

    @Autowired
    private RefreshTokensRepository refreshTokensRepository;

    @Autowired
    private AdvertisersRepository advertisersRepository;

    /**
     * Generates a new long-lived Refresh Token (UUID) for the given advertiser,
     * valid for 7 days.
     */
    public RefreshTokens createRefreshToken(Long advertiserId) {
        RefreshTokens refreshToken = new RefreshTokens();

        Optional<Advertisers> opt = advertisersRepository.findById(advertiserId);
        if (opt.isEmpty()) return null;

        refreshToken.setAdvertisers(opt.get());
        refreshToken.setExpiresAt(Timestamp.from(Instant.now().plusMillis(refreshTokenDurationMs)));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokensRepository.save(refreshToken);
        return refreshToken;
    }

    /**
     * Checks if the provided Refresh Token has expired.
     * If expired, it deletes the token from the DB to prevent clutter, and throws an Exception.
     */
    public RefreshTokens verifyExpiration(RefreshTokens token) {
        if (token.getExpiresAt().before(Timestamp.from(Instant.now()))) {
            refreshTokensRepository.delete(token);
            throw new RuntimeException("Refresh token is expired. Please make a new signin request");
        }
        return token;
    }

    public Optional<RefreshTokens> findByToken(String token) {
        return refreshTokensRepository.findByToken(token);
    }
    
    /**
     * Deletes all refresh tokens for a specific advertiser.
     * 
     * @Transactional ensures that the custom delete query is executed within a database transaction,
     * maintaining ACID properties (Atomicity, Consistency, Isolation, Durability).
     */
    @Transactional
    public void deleteByAdvertiserId(Long advertiserId) {
        refreshTokensRepository.deleteByAdvertisers_Id(advertiserId);
    }
}
