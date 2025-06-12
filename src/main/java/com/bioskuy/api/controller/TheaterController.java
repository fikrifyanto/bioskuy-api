package com.bioskuy.api.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bioskuy.api.common.ApiResponse;
import com.bioskuy.api.common.ResponseUtil;
import com.bioskuy.api.model.Movie;
import com.bioskuy.api.model.ShowingSchedule;
import com.bioskuy.api.model.Theater;
import com.bioskuy.api.service.MovieService;
import com.bioskuy.api.service.ScheduleService;
import com.bioskuy.api.service.TheaterService;

@RestController
@RequestMapping("/theater")
public class TheaterController {
    private final MovieService movieService;
    private final TheaterService theaterService;
    private final ScheduleService scheduleService;

    @Autowired
    public TheaterController(TheaterService theaterService, ScheduleService scheduleService, MovieService movieService) {
        this.movieService = movieService;
        this.theaterService = theaterService;
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Theater>>> getAllTheater(){
        List<Theater> theaters = theaterService.getAllTheater();
        return ResponseEntity.ok(ResponseUtil.success("Retreived " + theaters.size() + " Theater(s)", theaters));
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<ApiResponse<List<Theater>>> getTheatersfromMovie(@PathVariable Long id){
        Set<Theater> theaters = new HashSet<>();
        Theater theater;
        Movie movie = movieService.getMoviebyId(id);

        List<ShowingSchedule> movieSchedule = scheduleService.getAllSchedulebyMovie(movie);

        for(ShowingSchedule show : movieSchedule){
            try {
                theater = theaterService.getTheaterbyId(show.getTheater().getTheater_id());
                theaters.add(theater);
            } catch (IllegalArgumentException e) {
                if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtil.error(e.getMessage()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtil.error("Invalid input: " + e.getMessage()));
            }
            }
        }

        List<Theater> theaterList = new ArrayList<>(theaters);

        return ResponseEntity.ok(ResponseUtil.success("Retreived " + theaterList.size() + " Theater(s) with Movie id " + id, theaterList));
    }
}
