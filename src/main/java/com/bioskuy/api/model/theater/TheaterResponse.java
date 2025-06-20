package com.bioskuy.api.model.theater;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TheaterResponse {

    private Long id;

    private String name;

    private String address;

    private Integer capacity;
}
