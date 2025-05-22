package com.bioskuy.api.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.model.ShowingSchedule;

@Repository
public interface ScheduleRepository extends JpaRepository<ShowingSchedule, Long> {

    List<ShowingSchedule> findScheduleByMovie(Long movieId);

    List<ShowingSchedule> findScheduleByTheater(Long theaterId);

    List<ShowingSchedule> findScheduleByDate(LocalDate showingDate);

    List<ShowingSchedule> findScheduleByTime(LocalTime showingTime);

    List<ShowingSchedule> findScheduleByPrice(double ticketPrice);

    Optional<ShowingSchedule> findScheduleByTheaterandDate(Long theaterId, LocalDate showingDate);
}