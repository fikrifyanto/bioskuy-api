package com.bioskuy.api.controller;

import java.util.List;

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
import com.bioskuy.api.model.Theater;
import com.bioskuy.api.service.MovieService;
import com.bioskuy.api.service.TheaterService;

@RestController
@RequestMapping("/theaters")
public class TheaterController {
    private final MovieService movieService;
    private final TheaterService theaterService;

    @Autowired
    public TheaterController(TheaterService theaterService, MovieService movieService) {
        this.movieService = movieService;
        this.theaterService = theaterService;
    }

    /**
     * Get all theaters with pagination
     *
     * @param page      Page number (0-based)
     * @param size      Page size
     * @param sortBy    Field to sort by (default: "id")
     * @param direction Sort direction (default: "asc")
     * @return ResponseEntity with ApiResponse containing the page of theaters
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllTheaters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Theater> theaterPage = theaterService.getTheatersPaginated(pageable);

        return ResponseEntity.ok(ResponseUtil.success(
                "Retrieved " + theaterPage.getNumberOfElements() + " Theater(s) (Page " + (page + 1) + " of " + theaterPage.getTotalPages() + ")",
                theaterPage));
    }

    /**
     * Get theaters showing a specific movie
     *
     * @param id The ID of the movie
     * @return ResponseEntity with ApiResponse containing the list of theaters showing the movie
     */
    @GetMapping("/movie/{id}")
    public ResponseEntity<ApiResponse<List<Theater>>> getTheatersByMovieId(@PathVariable Long id) {
        // Check if a movie exists
        Movie movie = movieService.getMovieById(id);

        // Get theaters showing the movie
        List<Theater> theaters = theaterService.getTheatersByMovieId(id);

        if (theaters.isEmpty()) {
            return ResponseEntity.ok(ResponseUtil.success("No theaters found showing movie: " + movie.getTitle(), theaters));
        }

        return ResponseEntity.ok(ResponseUtil.success("Found " + theaters.size() + " theater(s) showing movie: " + movie.getTitle(), theaters));
    }
}
