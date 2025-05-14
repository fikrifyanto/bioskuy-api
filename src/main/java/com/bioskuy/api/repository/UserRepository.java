package com.bioskuy.api.repository;

import com.bioskuy.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA repository for User entities.
 * Provides database operations for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find the user by email
     * 
     * @param email User email
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findByEmail(String email);
}
