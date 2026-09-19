package com.kingroly.authservice.model;
// Generated 18 set 2026, 10:37:53 by Hibernate Tools 6.2.8.Final


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.IDENTITY;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;

/**
 * The Advertisers entity represents a registered advertising user or agency 
 * within the In-Game Ad Engine platform.
 * 
 * DESIGN PATTERN: This class follows the Database-First JPA/Hibernate mapping approach.
 * It is annotated with @Entity to indicate that it's a persistent JPA entity, 
 * and @Table to specify the exact database table ("advertisers") and catalog ("auth_db") it maps to.
 * 
 * Note the @UniqueConstraint annotations: these ensure that the database will reject 
 * any duplicate entries for 'advertiser_name' or 'email', protecting data integrity at the DB level.
 */
@Entity
@Table(name="advertisers"
    ,catalog="auth_db"
    , uniqueConstraints = {@UniqueConstraint(columnNames="advertiser_name"), @UniqueConstraint(columnNames="email")} 
)
public class Advertisers  implements java.io.Serializable {


     /** Primary Key: Unique identifier for the advertiser. */
     private Long id;
     /** The name of the advertising company or individual. Mapped to 'advertiser_name'. */
     private String advertiserName;
     /** The login email address. Must be unique. */
     private String email;
     /** The hashed password used for authentication. Never stored in plain text. */
     private String password;
     /** The authorization role (e.g., 'ROLE_ADVERTISER', 'ROLE_ADMIN'). */
     private String role;
     // --- JPA RELATIONSHIPS ---
     // The following Sets represent One-To-Many relationships. 
     // One Advertiser can have multiple Refresh Tokens, OTP Codes, Device Sessions, and Password Reset Tokens.
     // They are initialized as empty HashSets to avoid NullPointerExceptions when adding items.
     private Set<RefreshTokens> refreshTokenses = new HashSet<RefreshTokens>(0);
     private Set<OtpCodes> otpCodeses = new HashSet<OtpCodes>(0);
     private Set<DeviceSessions> deviceSessionses = new HashSet<DeviceSessions>(0);
     private Set<PasswordResetTokens> passwordResetTokenses = new HashSet<PasswordResetTokens>(0);

    public Advertisers() {
    }

    public Advertisers(String advertiserName, String email, String password, String role, Set<RefreshTokens> refreshTokenses, Set<OtpCodes> otpCodeses, Set<DeviceSessions> deviceSessionses, Set<PasswordResetTokens> passwordResetTokenses) {
       this.advertiserName = advertiserName;
       this.email = email;
       this.password = password;
       this.role = role;
       this.refreshTokenses = refreshTokenses;
       this.otpCodeses = otpCodeses;
       this.deviceSessionses = deviceSessionses;
       this.passwordResetTokenses = passwordResetTokenses;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="id", unique=true, nullable=false)
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    
    @Column(name="advertiser_name", unique=true, nullable=false, length=255)
    public String getAdvertiserName() {
        return this.advertiserName;
    }
    
    public void setAdvertiserName(String advertiserName) {
        this.advertiserName = advertiserName;
    }

    
    @Column(name="email", unique=true, nullable=false, length=255)
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    
    @Column(name="password", nullable=false, length=255)
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    
    @Column(name="role", length=50)
    public String getRole() {
        return this.role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @OneToMany mapping for refresh tokens.
     * fetch=FetchType.LAZY means that the tokens are only loaded from the database 
     * when this getter is explicitly called, saving memory and improving performance.
     * mappedBy="advertisers" indicates that the 'advertisers' property in the RefreshTokens 
     * entity owns the foreign key relationship.
     */
    @OneToMany(fetch=FetchType.LAZY, mappedBy="advertisers")
    public Set<RefreshTokens> getRefreshTokenses() {
        return this.refreshTokenses;
    }
    
    public void setRefreshTokenses(Set<RefreshTokens> refreshTokenses) {
        this.refreshTokenses = refreshTokenses;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="advertisers")
    public Set<OtpCodes> getOtpCodeses() {
        return this.otpCodeses;
    }
    
    public void setOtpCodeses(Set<OtpCodes> otpCodeses) {
        this.otpCodeses = otpCodeses;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="advertisers")
    public Set<DeviceSessions> getDeviceSessionses() {
        return this.deviceSessionses;
    }
    
    public void setDeviceSessionses(Set<DeviceSessions> deviceSessionses) {
        this.deviceSessionses = deviceSessionses;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="advertisers")
    public Set<PasswordResetTokens> getPasswordResetTokenses() {
        return this.passwordResetTokenses;
    }
    
    public void setPasswordResetTokenses(Set<PasswordResetTokens> passwordResetTokenses) {
        this.passwordResetTokenses = passwordResetTokenses;
    }




}


