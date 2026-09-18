package com.kingroly.authservice.repository;

import com.kingroly.authservice.model.DeviceSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DeviceSessionsRepository extends JpaRepository<DeviceSessions, Long> {
    Optional<DeviceSessions> findByTokenId(String tokenId);
    List<DeviceSessions> findByAdvertisers_IdAndIsRevokedFalse(Long advertiserId);
}
