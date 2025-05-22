package com.bioskuy.api.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.model.ShowingSchedule;

@Repository
public interface ScheduleRepository extends JpaRepository<ShowingSchedule, Long> {

    /**
     * @param movieId
     * @return List of Schedules showing a certain movie if exist, empty list otherwise
     */
    List<ShowingSchedule> findScheduleByMovie(Long movieId);

    /**
     * @param theaterId
     * @return List of Schedules showing in a certain theater if exist, empty list otherwise
     */
    List<ShowingSchedule> findScheduleByTheater(Long theaterId);

    /**
     * @param showingDate
     * @return List of Schedules showing in a specific date if exist, empty list otherwise
     */
    List<ShowingSchedule> findScheduleByDate(LocalDate showingDate);

    /**
     * @param showingTime
     * @return List of Schedules showing in a specific timeframe if exist, empty list otherwise
     */
    List<ShowingSchedule> findScheduleByTime(LocalTime showingTime);

    /**
     * @param ticketPrice
     * @return List of Schedules based on price if exist, empty list otherwise
     */
    List<ShowingSchedule> findScheduleByPrice(double ticketPrice);

    /**
     * @param theaterId
     * @param showingDate
     * @return List of Schedules in specific Theater on certain date if exist, empty list otherwise
     */
    List<ShowingSchedule> findScheduleByTheaterandDate(Long theaterId, LocalDate showingDate);
}