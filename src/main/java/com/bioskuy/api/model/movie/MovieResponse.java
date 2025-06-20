package com.bioskuy.api.model.movie;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {
    private Long id;

    private String title;

    private String genre;

    private Float rating;

    private Integer duration;
}
