package com.kingroly.authservice.repository;

import com.kingroly.authservice.model.OtpCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpCodesRepository extends JpaRepository<OtpCodes, Long> {
    Optional<OtpCodes> findByTempTokenAndCode(String tempToken, String code);
}
