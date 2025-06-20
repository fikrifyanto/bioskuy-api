package com.bioskuy.api.model.schedule;

import com.bioskuy.api.model.movie.MovieResponse;
import com.bioskuy.api.model.theater.TheaterResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {
    private Long id;

    private MovieResponse movie;

    private TheaterResponse theater;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Float price;
}
