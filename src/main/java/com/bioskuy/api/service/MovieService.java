package com.bioskuy.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bioskuy.api.model.Movie;
import com.bioskuy.api.repository.MovieRepository;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    public Movie getMoviebyId(Long id){
        Movie movie = movieRepository.findMovieByMovieId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Movie not found"));
        return movie;
    }

    public Movie getMoviebyTitle(String title){
        Movie movie = movieRepository.findMovieByTitle(title)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Movie not found"));
        return movie;
    }

    public List<Movie> getMoviebyGenre(String genre){
        List<Movie> movies = movieRepository.findMovieByGenre(genre);
        if (movies.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie with Genre "+ genre +"not found");
        }
        return movies;
    }

}
