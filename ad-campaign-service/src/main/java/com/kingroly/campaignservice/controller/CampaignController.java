package com.kingroly.campaignservice.controller;

import com.kingroly.campaignservice.dto.BuyCampaignRequest;
import com.kingroly.campaignservice.model.Campaigns;
import com.kingroly.campaignservice.model.Games;
import com.kingroly.campaignservice.repository.CampaignsRepository;
import com.kingroly.campaignservice.repository.GamesRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Campaign Controller.
 * 
 * DESIGN PATTERN: REST Controller / MVC Pattern.
 * This controller handles HTTP requests related to advertising campaigns.
 * Notice that it does NOT perform JWT signature validation itself; 
 * that security concern is handled entirely by the JwtValidatorFilter.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/campaigns")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
public class CampaignController {

    @Autowired
    private CampaignsRepository campaignsRepository;

    @Autowired
    private GamesRepository gamesRepository;

    /**
     * Endpoint to purchase a new advertising campaign.
     * 
     * @param request The @Valid annotated payload, triggering bean validation (e.g. @NotNull, @Future).
     * @return A ResponseEntity containing either an HTTP 400 (Bad Request) if the game is not found, 
     *         or HTTP 200 (OK) if the campaign is successfully stored in the database.
     */
    @PostMapping("/buy")
    public ResponseEntity<String> buyCampaign(@Valid @RequestBody BuyCampaignRequest request) {
        
        Games game = gamesRepository.findById(request.getGameId())
                .orElse(null);

        if (game == null) {
            return ResponseEntity.badRequest().body("Error: The specified game does not exist!");
        }

        Campaigns campaign = new Campaigns();
        campaign.setGames(game);
        campaign.setAdvertiserId(request.getAdvertiserId());
        campaign.setTitle(request.getTitle());
        campaign.setBudget(request.getBudget());
        campaign.setStartDate(request.getStartDate());
        campaign.setEndDate(request.getEndDate());
        campaign.setStatus("PENDING");
        campaign.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        campaignsRepository.save(campaign);

        return ResponseEntity.ok("Campaign purchased successfully! Current status: PENDING. Awaiting approval (mock payment).");
    }
}
