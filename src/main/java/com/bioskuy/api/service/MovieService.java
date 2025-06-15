package com.bioskuy.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bioskuy.api.model.Movie;
import com.bioskuy.api.repository.MovieRepository;

/**
 * Service for movie-related operations
 */
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Get movie by ID
     *
     * @param id movie ID
     * @return Movie with the given ID
     * @throws ResponseStatusException if movie not found
     */
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
    }

    /**
     * Get movies by combined filters with pagination
     *
     * @param title    Filter by title (optional)
     * @param genre    Filter by genre (optional)
     * @param rating   Filter by rating (optional)
     * @param pageable pagination information
     * @return Page of movies matching all provided filters
     */
    public Page<Movie> getMoviesByFilters(String title, String genre, Double rating, Pageable pageable) {
        return movieRepository.findMoviesByFilters(title, genre, rating, pageable);
    }
}
