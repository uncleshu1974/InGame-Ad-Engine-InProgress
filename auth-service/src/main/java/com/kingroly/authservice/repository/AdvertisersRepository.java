package com.kingroly.authservice.repository;

import com.kingroly.authservice.model.Advertisers;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for the Advertisers entity.
 * 
 * DESIGN PATTERN: Repository Pattern (Data Access Object - DAO).
 * By extending JpaRepository, Spring Data JPA automatically provides standard CRUD 
 * operations (save, findAll, delete, etc.) without requiring any boilerplate SQL or implementation classes.
 */

public interface AdvertisersRepository extends JpaRepository<Advertisers, Long> {
    /**
     * Query Derivation: Spring Data automatically parses the method name 'findByEmail'
     * and generates the underlying SQL: SELECT * FROM advertisers WHERE email = ?
     * 
     * @param email The email address to search for.
     * @return An Optional wrapping the Advertiser if found, avoiding NullPointerExceptions.
     */
    Optional<Advertisers> findByEmail(String email);
}
