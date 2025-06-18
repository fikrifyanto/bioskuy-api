package com.bioskuy.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.enums.SeatStatus;
import com.bioskuy.api.model.Seat;
import com.bioskuy.api.model.Schedule;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {


    /**
     * @param status Enums SeatStatus (see model/enums.java)
     * @return List of Seat based on status if existed, empty list otherwise
     */
    List<Seat> findByStatus(SeatStatus status);

    /**
     * @param schedule The schedule objects to find seats for
     * @return List of Seat based on schedule if existed, empty list otherwise
     */
    List<Seat> findBySchedule(Schedule schedule);

    /**
     * @param seatNumber The seat number to search for
     * @return Optional Seat if existed, empty otherwise
     */
    Optional<Seat> findBySeatNumber(String seatNumber);
}
