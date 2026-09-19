package com.kingroly.authservice.model;
// Generated 18 set 2026, 10:37:53 by Hibernate Tools 6.2.8.Final


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import java.sql.Timestamp;

/**
 * The RefreshTokens entity is used for maintaining long-lived user sessions.
 * 
 * When a short-lived JWT expires, the client can present a Refresh Token to get a new JWT 
 * without asking the user to log in again. Like other tokens, it has a strict expiration date.
 */
@Entity
@Table(name="refresh_tokens"
    ,catalog="auth_db"
    , uniqueConstraints = @UniqueConstraint(columnNames="token") 
)
public class RefreshTokens  implements java.io.Serializable {


     private long id;
     private Advertisers advertisers;
     private String token;
     private Timestamp expiresAt;

    public RefreshTokens() {
    }

    public RefreshTokens(long id, Advertisers advertisers, String token, Timestamp expiresAt) {
       this.id = id;
       this.advertisers = advertisers;
       this.token = token;
       this.expiresAt = expiresAt;
    }
   
     @Id 
     @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    
    @Column(name="id", unique=true, nullable=false)
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @ManyToOne defines the owner of this refresh token.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="advertiser_id", nullable=false)
    public Advertisers getAdvertisers() {
        return this.advertisers;
    }
    
    public void setAdvertisers(Advertisers advertisers) {
        this.advertisers = advertisers;
    }

    
    @Column(name="token", unique=true, nullable=false, length=255)
    public String getToken() {
        return this.token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="expires_at", nullable=false, length=19)
    public Timestamp getExpiresAt() {
        return this.expiresAt;
    }
    
    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }




}


