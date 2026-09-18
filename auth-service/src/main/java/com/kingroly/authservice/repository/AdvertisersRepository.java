package com.kingroly.authservice.repository;

import com.kingroly.authservice.model.Advertisers;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdvertisersRepository extends JpaRepository<Advertisers, Long> {
    Optional<Advertisers> findByEmail(String email);
}
