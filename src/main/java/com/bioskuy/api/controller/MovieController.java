package com.bioskuy.api.controller;

import com.bioskuy.api.model.movie.MovieResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bioskuy.api.common.ApiResponse;
import com.bioskuy.api.common.ResponseUtil;
import com.bioskuy.api.service.MovieService;

/**
 * Controller for movie-related endpoints.
 */
@RestController
@CrossOrigin
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Get all movies with optional pagination and filtering
     * Filters can be combined to narrow down results (title AND genre AND rating)
     *
     * @param page      Page number (0-based)
     * @param size      Page size
     * @param title     Filter by title (optional)
     * @param genre     Filter by genre (optional)
     * @param rating    Filter by rating (optional)
     * @param sortBy    Field to sort by (default: "id")
     * @param direction Sort direction (default: "asc")
     * @return ResponseEntity with ApiResponse containing the page of movies
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Double rating,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        // Apply filters
        Page<MovieResponse> moviePage = movieService.getMoviesByFilters(
                title,
                genre,
                rating,
                pageable
        );

        return ResponseEntity.ok(ResponseUtil.success(
                "Retrieved " + moviePage.getNumberOfElements() + " Movie(s) (Page " + (page + 1) + " of " + moviePage.getTotalPages() + ")",
                moviePage));
    }

    /**
     * Get movie by ID
     *
     * @param id Movie ID
     * @return ResponseEntity with ApiResponse containing the movie
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MovieResponse>> getMovieById(@PathVariable Long id) {
        MovieResponse movie = movieService.getMovieById(id);
        return ResponseEntity.ok(ResponseUtil.success("Found movie with ID " + id, movie));
    }
}
