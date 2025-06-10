package com.bioskuy.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * @param id Movie id
     * @return Optional containing Movie based on id if exist, empty otherwise
     */
    Optional<Movie> findMovieByMovieId(Long id);

    /**
     * @param title Movie title
     * @return Optional containing Movie based on title if exist, empty otherwise
     */
    Optional<Movie> findMovieByTitle(String title);

    /**
     * @param genre movie genre
     * @return List of movie based on genre if exist, empty list otherwise
     */
    List<Movie> findMovieByGenre(String genre);

    /**
     * @param rating movie rating
     * @return List of movie based on rating if exist, empty list otherwise
     */
    List<Movie> findMovieByRating(double rating);
}