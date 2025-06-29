package com.bioskuy.api.service;

import java.util.List;
import java.util.stream.Collectors;

import com.bioskuy.api.model.seat.SeatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bioskuy.api.entity.Seat;
import com.bioskuy.api.entity.Schedule;
import com.bioskuy.api.repository.ScheduleRepository;
import com.bioskuy.api.repository.SeatRepository;

@Service
public class SeatService implements  SeatServiceInterface{
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository, ScheduleRepository scheduleRepository) {
        this.seatRepository = seatRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public List<SeatResponse> getSeatsByScheduleId(Long id){
        Schedule schedule = scheduleRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Schedule not found"));

        return seatRepository.findBySchedule(schedule)
                .stream()
                .map(this::toSeatResponse)
                .collect(Collectors.toList());
    }

    public SeatResponse toSeatResponse(Seat seat){
        return SeatResponse.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .status(seat.getStatus())
                .build();
    }
}
