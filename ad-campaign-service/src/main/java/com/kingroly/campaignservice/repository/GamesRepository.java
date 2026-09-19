package com.kingroly.campaignservice.repository;

import com.kingroly.campaignservice.model.Games;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Data Access to the Games table.
 * Extends JpaRepository to inherit standard CRUD operations.
 */

public interface GamesRepository extends JpaRepository<Games, Long> {
}
