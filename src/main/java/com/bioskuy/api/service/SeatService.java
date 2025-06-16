package com.bioskuy.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bioskuy.api.model.Seat;
import com.bioskuy.api.model.Schedule;
import com.bioskuy.api.repository.ScheduleRepository;
import com.bioskuy.api.repository.SeatRepository;

@Service
public class SeatService {
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository, ScheduleRepository scheduleRepository) {
        this.seatRepository = seatRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public List<Seat> getAllSeat(){
        return seatRepository.findAll();
    }

    public List<Seat> getSeatsbySchedule(Long id){
        Schedule schedule = scheduleRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Schedule not found"));

        return seatRepository.findBySchedule(schedule);

    }

}
