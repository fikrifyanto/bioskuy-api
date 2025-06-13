package com.bioskuy.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.enums.SeatStatus;
import com.bioskuy.api.model.Seat;
import com.bioskuy.api.model.ShowingSchedule;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    
    
    /**
     * @param status Enums SeatStatus (see model/enums.java)
     * @return List of Seat based on status if exist, empty list otherwise
     */
    List<Seat> findByStatus(SeatStatus status);

    /**
     * @param schedule
     * @return List of Seat based on schedule if exist, empty list otherwise
     */
    List<Seat> findBySchedule(ShowingSchedule schedule);

    /**
     * @param seatNumber
     * @return Optional Seat if exist, empty otherwise
     */
    Optional<Seat> findBySeatNumber(String seatNumber);
}
