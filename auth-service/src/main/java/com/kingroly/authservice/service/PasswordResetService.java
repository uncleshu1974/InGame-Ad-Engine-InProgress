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

@Service
public class PasswordResetService {

    private final Long resetTokenDurationMs = 900000L; // 15 minuti

    @Autowired
    private PasswordResetTokensRepository passwordResetTokensRepository;

    @Autowired
    private AdvertisersRepository advertisersRepository;

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
