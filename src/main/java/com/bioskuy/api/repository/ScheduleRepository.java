package com.bioskuy.api.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.model.Movie;
import com.bioskuy.api.model.Schedule;
import com.bioskuy.api.model.Theater;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /**
     * @param movie the movie for which to find schedules
     * @return List of Schedules showing a certain movie if existed, empty list otherwise
     */
    List<Schedule> findScheduleByMovie(Movie movie);

    /**
     * @param movie the movie for which to find schedules
     * @param pageable pagination information
     * @return Page of Schedules showing a certain movie if existed, empty page otherwise
     */
    Page<Schedule> findScheduleByMovie(Movie movie, Pageable pageable);

    /**
     * @param theater the theater for which to find schedules
     * @return List of Schedules showing in a certain theater if existed, empty list otherwise
     */
    List<Schedule> findScheduleByTheater(Theater theater);

    /**
     * @param theater the theater for which to find schedules
     * @param pageable pagination information
     * @return Page of Schedules showing in a certain theater if existed, empty page otherwise
     */
    Page<Schedule> findScheduleByTheater(Theater theater, Pageable pageable);

    /**
     * @param showingDate the date for which to find schedules
     * @return List of Schedules showing in a specific date if existed, empty list otherwise
     */
    List<Schedule> findScheduleByShowingDate(LocalDate showingDate);

    /**
     * @param showingTime the time for which to find schedules
     * @return List of Schedules showing in a specific timeframe if existed, empty list otherwise
     */
    List<Schedule> findScheduleByShowingTime(LocalTime showingTime);

    /**
     * @param ticketPrice the price for which to find schedules
     * @return List of Schedules based on price if existed, empty list otherwise
     */
    List<Schedule> findScheduleByTicketPrice(double ticketPrice);

    /**
     * @param pageable pagination information
     * @return Page of all Schedules
     */
    Page<Schedule> findAll(Pageable pageable);

}
