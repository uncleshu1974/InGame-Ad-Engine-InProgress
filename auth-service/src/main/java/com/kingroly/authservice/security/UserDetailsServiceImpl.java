package com.kingroly.authservice.security;

import com.kingroly.authservice.model.Advertisers;
import com.kingroly.authservice.repository.AdvertisersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of Spring Security's UserDetailsService interface.
 * 
 * This service is called by the AuthenticationManager during the login process to fetch 
 * the user's data from the database based on the provided username (which in our case is the email).
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AdvertisersRepository advertisersRepository;

    /**
     * Loads the user data from the AdvertisersRepository.
     * If the user is found, it uses the UserDetailsImpl.build() factory method 
     * to convert the JPA entity into a Spring Security UserDetails object.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Advertisers advertiser = advertisersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return UserDetailsImpl.build(advertiser);
    }
}
