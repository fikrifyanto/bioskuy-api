package com.bioskuy.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.model.Theater;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    
    /**
     * @param location
     * @return List of all Theater in a location if exist, empty list otherwise
     */
    List<Theater> findByLocation(String location);

    /**
     * @param id
     * @param location
     * @return Optional specific Theater based on id and location if exist, empty list otherwise
     */
    Optional<Theater> findByIdandLocation(Long id, String location);
}
