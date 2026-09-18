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

@Service
public class RefreshTokenService {

    private final Long refreshTokenDurationMs = 604800000L; // 7 giorni

    @Autowired
    private RefreshTokensRepository refreshTokensRepository;

    @Autowired
    private AdvertisersRepository advertisersRepository;

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
    
    @Transactional
    public void deleteByAdvertiserId(Long advertiserId) {
        refreshTokensRepository.deleteByAdvertisers_Id(advertiserId);
    }
}
