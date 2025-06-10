package com.bioskuy.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @Column(name = "movie_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "genre", nullable = false)
    private String genre;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "rating", nullable = false)
    private double rating;

    public Movie(String title, String genre, int duration, double rating){
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
    }
}
