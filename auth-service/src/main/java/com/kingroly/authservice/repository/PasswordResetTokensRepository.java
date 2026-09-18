package com.kingroly.authservice.repository;

import com.kingroly.authservice.model.PasswordResetTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokensRepository extends JpaRepository<PasswordResetTokens, Long> {
    Optional<PasswordResetTokens> findByToken(String token);
}
