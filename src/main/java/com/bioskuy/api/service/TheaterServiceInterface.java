package com.bioskuy.api.service;

import com.bioskuy.api.model.theater.TheaterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TheaterServiceInterface {
    TheaterResponse getTheaterbyId(Long id);
    Page<TheaterResponse> getTheatersPaginated(Pageable pageable);
    List<TheaterResponse> getTheatersByMovieId(Long movieId);
}
