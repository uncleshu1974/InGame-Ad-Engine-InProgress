package com.kingroly.authservice.security;

import com.kingroly.authservice.model.Advertisers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

/**
 * Custom implementation of Spring Security's UserDetails interface.
 * 
 * DESIGN PATTERN: Adapter Pattern.
 * Spring Security doesn't know about our custom 'Advertisers' entity. 
 * This class adapts our Advertisers entity into a standard UserDetails object 
 * that Spring Security's AuthenticationManager can understand and use.
 */
public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private String advertiserName;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String email, String password, String advertiserName, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.advertiserName = advertiserName;
        this.authorities = authorities;
    }

    /**
     * Factory method to build a UserDetailsImpl object directly from an Advertisers entity.
     * It maps the entity's fields (like email and role) to the standard Spring Security fields.
     */
    public static UserDetailsImpl build(Advertisers advertiser) {
        return new UserDetailsImpl(
                advertiser.getId(),
                advertiser.getEmail(),
                advertiser.getPassword(),
                advertiser.getAdvertiserName(),
                Collections.singletonList(new SimpleGrantedAuthority(advertiser.getRole() != null ? advertiser.getRole() : "ROLE_ADVERTISER"))
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return email; } 

    public Long getId() { return id; }
    public String getAdvertiserName() { return advertiserName; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
