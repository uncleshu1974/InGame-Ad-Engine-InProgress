package com.kingroly.campaignservice.repository;

import com.kingroly.campaignservice.model.Campaigns;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Data Access to the Campaigns table.
 * Extends JpaRepository to inherit standard CRUD operations.
 */

public interface CampaignsRepository extends JpaRepository<Campaigns, Long> {
}
