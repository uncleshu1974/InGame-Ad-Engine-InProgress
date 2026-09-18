package com.kingroly.authservice.security;

import com.kingroly.authservice.model.Advertisers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private String companyName;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String email, String password, String companyName, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.companyName = companyName;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(Advertisers advertiser) {
        return new UserDetailsImpl(
                advertiser.getId(),
                advertiser.getEmail(),
                advertiser.getPassword(),
                advertiser.getCompanyName(),
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
    public String getCompanyName() { return companyName; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
