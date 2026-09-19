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
 * The OtpCodes entity stores the 6-digit One-Time Passwords used for Two-Factor Authentication (2FA).
 * 
 * When a user logs in, they receive a 'tempToken' and an email with the 'code'. 
 * They must submit both before 'expiresAt' to receive their final JWT.
 * The @UniqueConstraint on 'temp_token' ensures tokens cannot be reused or duplicated.
 */
@Entity
@Table(name="otp_codes"
    ,catalog="auth_db"
    , uniqueConstraints = @UniqueConstraint(columnNames="temp_token") 
)
public class OtpCodes  implements java.io.Serializable {


     private Long id;
     private Advertisers advertisers;
     private String tempToken;
     private String code;
     private Timestamp expiresAt;
     private Boolean isUsed;

    public OtpCodes() {
    }

    public OtpCodes(Advertisers advertisers, String tempToken, String code, Timestamp expiresAt, Boolean isUsed) {
       this.advertisers = advertisers;
       this.tempToken = tempToken;
       this.code = code;
       this.expiresAt = expiresAt;
       this.isUsed = isUsed;
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
     * @ManyToOne mapping indicating the owner (Advertiser) of this OTP.
     * fetch=FetchType.LAZY ensures the Advertiser is fetched from the DB only on demand.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="advertiser_id", nullable=false)
    public Advertisers getAdvertisers() {
        return this.advertisers;
    }
    
    public void setAdvertisers(Advertisers advertisers) {
        this.advertisers = advertisers;
    }

    
    @Column(name="temp_token", unique=true, nullable=false, length=255)
    public String getTempToken() {
        return this.tempToken;
    }
    
    public void setTempToken(String tempToken) {
        this.tempToken = tempToken;
    }

    
    @Column(name="code", nullable=false, length=10)
    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="expires_at", nullable=false, length=19)
    public Timestamp getExpiresAt() {
        return this.expiresAt;
    }
    
    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }

    
    @Column(name="is_used")
    public Boolean getIsUsed() {
        return this.isUsed;
    }
    
    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }




}


