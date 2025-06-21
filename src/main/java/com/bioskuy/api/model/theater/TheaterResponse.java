package com.bioskuy.api.model.theater;

import com.bioskuy.api.model.schedule.ScheduleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TheaterResponse {

    private Long id;

    private String name;

    private String address;

    private Integer capacity;

    private List<ScheduleResponse> schedules;
}
