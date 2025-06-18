package com.bioskuy.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.entity.Movie;

/**
 * Repository for Movie entities
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * Find movies by combined filters (title, genre, rating)
     *
     * @param title    part of the movie title (optional)
     * @param genre    movie genre (optional)
     * @param rating   movie rating (optional)
     * @param pageable pagination information
     * @return Page of movies matching all provided filters
     */
    @Query("SELECT m FROM Movie m WHERE " +
            "(:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:genre IS NULL OR m.genre = :genre) AND " +
            "(:rating IS NULL OR m.rating = :rating)")
    Page<Movie> findMoviesByFilters(
            @Param("title") String title,
            @Param("genre") String genre,
            @Param("rating") Double rating,
            Pageable pageable);
}