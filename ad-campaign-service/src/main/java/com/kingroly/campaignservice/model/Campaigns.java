package com.kingroly.campaignservice.model;
// Generated 17 set 2026, 10:48:50 by Hibernate Tools 6.2.8.Final

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * The Campaigns entity maps to the 'campaigns' table in the 'campaign_db'.
 * 
 * It represents an advertising campaign created by an advertiser (from auth-service)
 * to be displayed within a specific game. 
 * Since this is a microservices architecture, we store the 'advertiserId' as a simple long
 * rather than a @ManyToOne relationship to the auth_db, maintaining database isolation.
 */
@Entity
@Table(name="campaigns", catalog="campaign_db")
public class Campaigns implements java.io.Serializable {

     /** Primary Key */
     private long id;
     /** 
      * @ManyToOne relationship to the Games entity. 
      * A single game can host multiple ad campaigns. 
      */
     private Games games;
     private long advertiserId;
     private String title;
     private BigDecimal budget;
     private String status;
     private Date startDate;
     private Date endDate;
     private Timestamp createdAt;

    public Campaigns() {
    }

    public Campaigns(long id, Games games, long advertiserId, String title, BigDecimal budget, Date startDate, Date endDate) {
        this.id = id;
        this.games = games;
        this.advertiserId = advertiserId;
        this.title = title;
        this.budget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Campaigns(long id, Games games, long advertiserId, String title, BigDecimal budget, String status, Date startDate, Date endDate, Timestamp createdAt) {
       this.id = id;
       this.games = games;
       this.advertiserId = advertiserId;
       this.title = title;
       this.budget = budget;
       this.status = status;
       this.startDate = startDate;
       this.endDate = endDate;
       this.createdAt = createdAt;
    }
   
     @Id 
    @Column(name="id", unique=true, nullable=false)
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="game_id", nullable=false)
    public Games getGames() {
        return this.games;
    }
    
    public void setGames(Games games) {
        this.games = games;
    }

    @Column(name="advertiser_id", nullable=false)
    public long getAdvertiserId() {
        return this.advertiserId;
    }
    
    public void setAdvertiserId(long advertiserId) {
        this.advertiserId = advertiserId;
    }

    @Column(name="title", nullable=false, length=255)
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name="budget", nullable=false, precision=10)
    public BigDecimal getBudget() {
        return this.budget;
    }
    
    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    @Column(name="status", length=50)
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="start_date", nullable=false, length=10)
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="end_date", nullable=false, length=10)
    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at", length=19)
    public Timestamp getCreatedAt() {
        return this.createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
