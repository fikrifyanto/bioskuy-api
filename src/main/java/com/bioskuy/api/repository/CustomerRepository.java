package com.bioskuy.api.repository;

import com.bioskuy.api.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA repository for User entities.
 * Provides database operations for the User entity.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Find the user by email
     * 
     * @param email User email
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<Customer> findByEmail(String email);
}
