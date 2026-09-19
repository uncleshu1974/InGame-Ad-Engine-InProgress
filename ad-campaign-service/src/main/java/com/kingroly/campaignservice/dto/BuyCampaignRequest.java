package com.kingroly.campaignservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Data Transfer Object (DTO) for creating a new Advertising Campaign.
 * 
 * DESIGN PATTERN: Data Transfer Object.
 * Used to securely capture incoming JSON payloads and automatically validate the inputs 
 * via Java Bean Validation annotations (@NotNull, @Future, etc.) before the controller logic runs.
 */
public class BuyCampaignRequest {
    
    @NotNull
    private Long gameId;
    
    @NotNull
    private Long advertiserId;
    
    @NotBlank
    private String title;
    
    /**
     * @Positive ensures the budget is strictly greater than zero.
     */
    @NotNull
    @Positive
    private BigDecimal budget;
    
    /**
     * @Future ensures that the campaign must start in the future, not in the past.
     */
    @NotNull
    @Future
    private Date startDate;
    
    /**
     * @Future ensures the end date is also in the future.
     * Note: In a complete application, custom validation could verify that endDate is after startDate.
     */
    @NotNull
    @Future
    private Date endDate;

    // Getters e Setters
    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }

    public Long getAdvertiserId() { return advertiserId; }
    public void setAdvertiserId(Long advertiserId) { this.advertiserId = advertiserId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
}
