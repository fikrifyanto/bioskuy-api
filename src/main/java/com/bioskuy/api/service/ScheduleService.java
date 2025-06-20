package com.bioskuy.api.service;

import com.bioskuy.api.model.movie.MovieResponse;
import com.bioskuy.api.model.schedule.ScheduleResponse;
import com.bioskuy.api.model.theater.TheaterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bioskuy.api.entity.Schedule;
import com.bioskuy.api.repository.ScheduleRepository;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Page<ScheduleResponse> getAllSchedules(Long theaterId, Long movieId, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Schedule> schedules;

        if (theaterId != null && movieId != null) {
            schedules = scheduleRepository.findByTheaterIdAndMovieId(theaterId, movieId, pageable);
        } else if (theaterId != null) {
            schedules = scheduleRepository.findByTheaterId(theaterId, pageable);
        } else if (movieId != null) {
            schedules = scheduleRepository.findByMovieId(movieId, pageable);
        } else {
            schedules = scheduleRepository.findAll(pageable);
        }

        return schedules.map(this::toScheduleResponse);
    }

    public ScheduleResponse toScheduleResponse(Schedule schedule){
        MovieResponse movie = MovieResponse.builder()
                .id(schedule.getMovie().getId())
                .title(schedule.getMovie().getTitle())
                .genre(schedule.getMovie().getGenre())
                .duration(schedule.getMovie().getDuration())
                .rating(schedule.getMovie().getRating())
                .build();

        TheaterResponse theater = TheaterResponse.builder()
                .id(schedule.getTheater().getId())
                .name(schedule.getTheater().getName())
                .address(schedule.getTheater().getAddress())
                .capacity(schedule.getTheater().getCapacity())
                .build();

        return ScheduleResponse.builder()
                .id(schedule.getId())
                .movie(movie)
                .theater(theater)
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .price(schedule.getPrice())
                .build();
    }
}
