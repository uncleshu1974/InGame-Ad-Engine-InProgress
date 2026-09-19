package com.kingroly.authservice.repository;

import com.kingroly.authservice.model.RefreshTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing long-lived Refresh Tokens.
 */
@Repository
public interface RefreshTokensRepository extends JpaRepository<RefreshTokens, Long> {
    
    /**
     * Looks up a refresh token by its unique string value.
     */
    Optional<RefreshTokens> findByToken(String token);

    /**
     * Deletes all refresh tokens belonging to a specific advertiser.
     * Useful for forcing a global logout across all devices for a given user.
     */
    void deleteByAdvertisers_Id(Long advertiserId);
}
