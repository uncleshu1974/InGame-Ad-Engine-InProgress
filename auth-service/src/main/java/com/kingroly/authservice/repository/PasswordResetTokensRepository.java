package com.kingroly.authservice.repository;

import com.kingroly.authservice.model.PasswordResetTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Password Reset Tokens.
 * 
 * @Repository annotation is optional here since JpaRepository already includes it, 
 * but explicitly defining it improves code readability.
 */
@Repository
public interface PasswordResetTokensRepository extends JpaRepository<PasswordResetTokens, Long> {
    /**
     * Finds a password reset token by its exact string value.
     */
    Optional<PasswordResetTokens> findByToken(String token);
}
