package com.bioskuy.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bioskuy.api.model.ShowingSchedule;
import com.bioskuy.api.model.Theater;
import com.bioskuy.api.repository.ScheduleRepository;
import com.bioskuy.api.repository.TheaterRepository;

@Service
public class TheaterService{
    private final TheaterRepository theaterRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public TheaterService(TheaterRepository theaterRepository, ScheduleRepository scheduleRepository) {
        this.theaterRepository = theaterRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public Theater getTheaterbyId(Long id){
        return theaterRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Theater not found"));
    }

    /**
     * Get theaters with pagination
     *
     * @param pageable pagination information
     * @return Page of theaters
     */
    public Page<Theater> getTheatersPaginated(Pageable pageable) {
        return theaterRepository.findAll(pageable);
    }

    /**
     * Get theaters showing a specific movie
     *
     * @param movieId the ID of the movie
     * @return List of theaters showing the movie with their schedules for the movie
     */
    public List<Theater> getTheatersByMovieId(Long movieId) {
        // Find all schedules for the movie
        List<ShowingSchedule> movieSchedules = scheduleRepository.findAll().stream()
                .filter(schedule -> schedule.getMovie().getId().equals(movieId))
                .toList();

        // Extract unique theaters from the schedules
        Set<Theater> uniqueTheaters = new HashSet<>();
        for (ShowingSchedule schedule : movieSchedules) {
            uniqueTheaters.add(schedule.getTheater());
        }

        // For each theater, filter its schedules to only include those for the specified movie
        List<Theater> theaters = uniqueTheaters.stream().toList();
        for (Theater theater : theaters) {
            List<ShowingSchedule> theaterSchedulesForMovie = movieSchedules.stream()
                    .filter(schedule -> schedule.getTheater().getId().equals(theater.getId()))
                    .toList();
            theater.setSchedules(theaterSchedulesForMovie);
        }

        return theaters;
    }
}
