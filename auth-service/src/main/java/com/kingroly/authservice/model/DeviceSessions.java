package com.kingroly.authservice.model;
// Generated 18 set 2026, 10:37:53 by Hibernate Tools 6.2.8.Final


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.IDENTITY;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import java.sql.Timestamp;

/**
 * The DeviceSessions entity is used to track active logins and devices for a given advertiser.
 * This is crucial for security (e.g., viewing active sessions, revoking access remotely).
 * 
 * It maps to the 'device_sessions' table. The 'token_id' (usually the JWT signature or JTI) 
 * is set as a unique constraint to ensure each session maps 1:1 to a specific issued token.
 */
@Entity
@Table(name="device_sessions"
    ,catalog="auth_db"
    , uniqueConstraints = @UniqueConstraint(columnNames="token_id") 
)
public class DeviceSessions  implements java.io.Serializable {


     private Long id;
     private Advertisers advertisers;
     private String tokenId;
     private String deviceInfo;
     private String ipAddress;
     private Timestamp createdAt;
     private Timestamp lastUsedAt;
     private Boolean isRevoked;

    public DeviceSessions() {
    }

    public DeviceSessions(Advertisers advertisers, String tokenId, String deviceInfo, String ipAddress, Timestamp createdAt, Timestamp lastUsedAt, Boolean isRevoked) {
       this.advertisers = advertisers;
       this.tokenId = tokenId;
       this.deviceInfo = deviceInfo;
       this.ipAddress = ipAddress;
       this.createdAt = createdAt;
       this.lastUsedAt = lastUsedAt;
       this.isRevoked = isRevoked;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="id", unique=true, nullable=false)
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @ManyToOne defines the foreign key relationship to the Advertisers table.
     * Multiple DeviceSessions can belong to a single Advertiser.
     * The @JoinColumn specifies the physical column 'advertiser_id' used for the join.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="advertiser_id", nullable=false)
    public Advertisers getAdvertisers() {
        return this.advertisers;
    }
    
    public void setAdvertisers(Advertisers advertisers) {
        this.advertisers = advertisers;
    }

    
    @Column(name="token_id", unique=true, nullable=false, length=255)
    public String getTokenId() {
        return this.tokenId;
    }
    
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    
    @Column(name="device_info", nullable=false, length=255)
    public String getDeviceInfo() {
        return this.deviceInfo;
    }
    
    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    
    @Column(name="ip_address", nullable=false, length=45)
    public String getIpAddress() {
        return this.ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @Temporal(TemporalType.TIMESTAMP) tells Hibernate to map this java.sql.Timestamp
     * to a standard SQL DATETIME/TIMESTAMP column.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at", length=19)
    public Timestamp getCreatedAt() {
        return this.createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_used_at", length=19)
    public Timestamp getLastUsedAt() {
        return this.lastUsedAt;
    }
    
    public void setLastUsedAt(Timestamp lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    
    @Column(name="is_revoked")
    public Boolean getIsRevoked() {
        return this.isRevoked;
    }
    
    public void setIsRevoked(Boolean isRevoked) {
        this.isRevoked = isRevoked;
    }




}


