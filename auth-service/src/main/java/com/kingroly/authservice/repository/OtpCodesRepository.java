package com.kingroly.authservice.repository;

import com.kingroly.authservice.model.OtpCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for managing One-Time Password (OTP) codes in the database.
 */

public interface OtpCodesRepository extends JpaRepository<OtpCodes, Long> {
    /**
     * Finds an OTP code by matching both the temporary token AND the 6-digit code provided by the user.
     * Generates: SELECT * FROM otp_codes WHERE temp_token = ? AND code = ?
     */
    Optional<OtpCodes> findByTempTokenAndCode(String tempToken, String code);
}
