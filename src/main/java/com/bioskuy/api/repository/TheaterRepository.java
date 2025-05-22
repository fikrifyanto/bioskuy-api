package com.bioskuy.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.model.Theater;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    
    // list all theater in a location
    List<Theater> findByLocation(String location);

    // Find specific Theater based on id and location
    Optional<Theater> findByIdandLocation(Long id, String location);
}
