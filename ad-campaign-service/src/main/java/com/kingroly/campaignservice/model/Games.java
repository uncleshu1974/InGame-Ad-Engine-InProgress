package com.kingroly.campaignservice.model;
// Generated 17 set 2026, 10:48:50 by Hibernate Tools 6.2.8.Final

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * The Games entity represents a video game registered on the platform.
 * It maps to the 'games' table. 
 * Advertisers can choose to display their campaigns within these games.
 */
@Entity
@Table(name="games", catalog="campaign_db")
public class Games implements java.io.Serializable {

     private long id;
     private String title;
     private String genre;
     private String platform;
     private String developer;
     /** One-To-Many relationship indicating all campaigns targeting this game. */
     private Set<Campaigns> campaignses = new HashSet<Campaigns>(0);

    public Games() {
    }

    public Games(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Games(long id, String title, String genre, String platform, String developer, Set<Campaigns> campaignses) {
       this.id = id;
       this.title = title;
       this.genre = genre;
       this.platform = platform;
       this.developer = developer;
       this.campaignses = campaignses;
    }
   
     @Id 
    @Column(name="id", unique=true, nullable=false)
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    @Column(name="title", nullable=false, length=255)
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name="genre", length=100)
    public String getGenre() {
        return this.genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Column(name="platform", length=100)
    public String getPlatform() {
        return this.platform;
    }
    
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Column(name="developer", length=255)
    public String getDeveloper() {
        return this.developer;
    }
    
    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    @OneToMany(fetch=FetchType.LAZY, mappedBy="games")
    public Set<Campaigns> getCampaignses() {
        return this.campaignses;
    }
    
    public void setCampaignses(Set<Campaigns> campaignses) {
        this.campaignses = campaignses;
    }
}
