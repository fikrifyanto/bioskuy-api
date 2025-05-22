package com.bioskuy.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.model.Seat;
import com.bioskuy.api.model.enums.SeatStatus;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    
    // find all Seat with certain status (AVAILABLE, RESERVED, SOLD)
    List<Seat> findByStatus(SeatStatus status);

    // find specific seat by seatNumber
    Optional<Seat> findByNumber(String seatNumber);
}
