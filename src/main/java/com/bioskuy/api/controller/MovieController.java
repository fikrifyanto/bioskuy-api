package com.bioskuy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bioskuy.api.common.ApiResponse;
import com.bioskuy.api.common.ResponseUtil;
import com.bioskuy.api.model.Movie;
import com.bioskuy.api.service.MovieService;

/**
 * Controller for movie-related endpoints.
 */
@RestController
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
        Page<Movie> moviePage = movieService.getMoviesByFilters(
                (title != null && !title.isEmpty()) ? title : null,
                (genre != null && !genre.isEmpty()) ? genre : null,
                rating,
                pageable
        );

        return ResponseEntity.ok(ResponseUtil.success("Movies retrieved sucessfully", moviePage));
    }

    /**
     * Get movie by ID
     *
     * @param id Movie ID
     * @return ResponseEntity with ApiResponse containing the movie
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Movie>> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.ok(ResponseUtil.success("Found movie with ID " + id, movie));
    }
}
