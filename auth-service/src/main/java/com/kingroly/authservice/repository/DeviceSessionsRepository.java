package com.kingroly.authservice.repository;

import com.kingroly.authservice.model.DeviceSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Device Sessions.
 * Used to track which devices are currently logged in for security and auditing purposes.
 */

public interface DeviceSessionsRepository extends JpaRepository<DeviceSessions, Long> {
    /**
     * Finds a session based on the unique JWT identifier (tokenId).
     * Automatically generates: SELECT * FROM device_sessions WHERE token_id = ?
     */
    Optional<DeviceSessions> findByTokenId(String tokenId);

    /**
     * Finds all active (non-revoked) sessions for a specific advertiser.
     * The underscore '_' in 'findByAdvertisers_Id' is a Spring Data feature allowing it to traverse 
     * the 'Advertisers' relationship and query its 'id' property.
     * 
     * Generates: SELECT * FROM device_sessions WHERE advertiser_id = ? AND is_revoked = false
     */
    List<DeviceSessions> findByAdvertisers_IdAndIsRevokedFalse(Long advertiserId);
}
