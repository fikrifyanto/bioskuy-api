package com.bioskuy.api.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.model.Movie;
import com.bioskuy.api.model.Schedule;
import com.bioskuy.api.model.Theater;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /**
     * @param movie
     * @return List of Schedules showing a certain movie if exist, empty list otherwise
     */
    List<Schedule> findScheduleByMovie(Movie movie);

    /**
     * @param theater
     * @return List of Schedules showing in a certain theater if exist, empty list otherwise
     */
    List<Schedule> findScheduleByTheater(Theater theater);

    /**
     * @param showingDate
     * @return List of Schedules showing in a specific date if exist, empty list otherwise
     */
    List<Schedule> findScheduleByShowingDate(LocalDate showingDate);

    /**
     * @param showingTime
     * @return List of Schedules showing in a specific timeframe if exist, empty list otherwise
     */
    List<Schedule> findScheduleByShowingTime(LocalTime showingTime);

    /**
     * @param ticketPrice
     * @return List of Schedules based on price if exist, empty list otherwise
     */
    List<Schedule> findScheduleByTicketPrice(double ticketPrice);

}