package com.bioskuy.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bioskuy.api.model.schedule.ScheduleResponse;
import com.bioskuy.api.model.theater.TheaterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bioskuy.api.entity.Schedule;
import com.bioskuy.api.entity.Theater;
import com.bioskuy.api.repository.ScheduleRepository;
import com.bioskuy.api.repository.TheaterRepository;

@Service
public class TheaterService implements TheaterServiceInterface {
    private final TheaterRepository theaterRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public TheaterService(TheaterRepository theaterRepository, ScheduleRepository scheduleRepository) {
        this.theaterRepository = theaterRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public TheaterResponse getTheaterbyId(Long id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Theater not found"));

        return toTheaterResponse(theater);
    }


    /**
     * Get theaters with pagination
     *
     * @param pageable pagination information
     * @return Page of theaters
     */
    public Page<TheaterResponse> getTheatersPaginated(Pageable pageable) {
        return theaterRepository.findAll(pageable)
                .map(this::toTheaterResponse);
    }

    /**
     * Get theaters showing a specific movie
     *
     * @param movieId the ID of the movie
     * @return List of theaters showing the movie with their schedules for the movie
     */
    public List<TheaterResponse> getTheatersByMovieId(Long movieId) {
        // Find all schedules for the movie
        List<Schedule> movieSchedules = scheduleRepository.findAll().stream()
                .filter(schedule -> schedule.getMovie().getId().equals(movieId))
                .toList();

        // Extract unique theaters from the schedules
        Set<Theater> uniqueTheaters = new HashSet<>();
        for (Schedule schedule : movieSchedules) {
            uniqueTheaters.add(schedule.getTheater());
        }

        // For each theater, filter its schedules to only include those for the specified movie
        List<Theater> theaters = uniqueTheaters.stream().toList();
        for (Theater theater : theaters) {
            List<Schedule> theaterSchedulesForMovie = movieSchedules.stream()
                    .filter(schedule -> schedule.getTheater().getId().equals(theater.getId()))
                    .toList();
            theater.setSchedules(theaterSchedulesForMovie);
        }

        return theaters.stream()
                .map(this::toTheaterResponse)
                .toList();
    }

    public ScheduleResponse toScheduleResponse(Schedule schedule){
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .price(schedule.getPrice())
                .build();
    }

    public TheaterResponse toTheaterResponse(Theater theater) {
        List<ScheduleResponse> schedules = theater.getSchedules().stream()
                .map(this::toScheduleResponse)
                .toList();

        return TheaterResponse.builder()
                .id(theater.getId())
                .name(theater.getName())
                .address(theater.getAddress())
                .capacity(theater.getCapacity())
                .schedules(schedules)
                .build();
    }
}
