package com.bioskuy.api.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.model.Movie;
import com.bioskuy.api.model.ShowingSchedule;
import com.bioskuy.api.model.Theater;

@Repository
public interface ScheduleRepository extends JpaRepository<ShowingSchedule, Long> {

    /**
     * @param movie
     * @return List of Schedules showing a certain movie if exist, empty list otherwise
     */
    List<ShowingSchedule> findScheduleByMovie(Movie movie);

    /**
     * @param theater
     * @return List of Schedules showing in a certain theater if exist, empty list otherwise
     */
    List<ShowingSchedule> findScheduleByTheater(Theater theater);

    /**
     * @param showingDate
     * @return List of Schedules showing in a specific date if exist, empty list otherwise
     */
    List<ShowingSchedule> findScheduleByShowingDate(LocalDate showingDate);

    /**
     * @param showingTime
     * @return List of Schedules showing in a specific timeframe if exist, empty list otherwise
     */
    List<ShowingSchedule> findScheduleByShowingTime(LocalTime showingTime);

    /**
     * @param ticketPrice
     * @return List of Schedules based on price if exist, empty list otherwise
     */
    List<ShowingSchedule> findScheduleByTicketPrice(double ticketPrice);

}