package com.bioskuy.api.service;

import com.bioskuy.api.model.movie.MovieResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieServiceInterface {
    MovieResponse getMovieById(Long id);

    Page<MovieResponse> getMoviesByFilters(String title, String genre, Double rating, Pageable pageable);
}
