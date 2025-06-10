package com.bioskuy.api.controller;

import java.util.List;

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
import com.bioskuy.api.service.MovieService;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Movie>>> getMovies(){
        List<Movie> movies = movieService.getAllMovies();
        return ResponseEntity.ok(ResponseUtil.success("Retreived all "+ movies.size() +"  movie(s)", movies)); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Movie>> getMoviebyId(@PathVariable Long id){
        try {
            Movie movie = movieService.getMoviebyId(id);
            return ResponseEntity.ok(ResponseUtil.success("Found Movie with id " + id, movie));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.error(e.getMessage()));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.error("Invalid input: " + e.getMessage()));
            }
        }
        
    }

}
