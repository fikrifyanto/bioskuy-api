package com.bioskuy.api.dto;

import com.bioskuy.api.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private Long id;
    private String title;
    private String genre;
    private int duration;
    private double rating;

    public static MovieDTO fromMovie(Movie movie) {
        return new MovieDTO(
            movie.getId(),
            movie.getTitle(),
            movie.getGenre(),
            movie.getDuration(),
            movie.getRating()
        );
    }
}