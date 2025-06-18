package com.bioskuy.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.entity.Seat;
import com.bioskuy.api.entity.Schedule;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    /**
     * @param schedule The schedule objects to find seats for
     * @return List of Seat based on schedule if existed, empty list otherwise
     */
    List<Seat> findBySchedule(Schedule schedule);
}
