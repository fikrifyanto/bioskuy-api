package com.bioskuy.api.dto;

import com.bioskuy.api.model.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {
    private Long id;
    private MovieDTO movie;
    private LocalDate showingDate;
    private LocalTime showingTime;
    private double ticketPrice;

    public static ScheduleDTO fromSchedule(Schedule schedule) {
        return new ScheduleDTO(
            schedule.getId(),
            MovieDTO.fromMovie(schedule.getMovie()),
            schedule.getShowingDate(),
            schedule.getShowingTime(),
            schedule.getTicketPrice()
        );
    }
}
