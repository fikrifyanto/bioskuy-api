package com.bioskuy.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bioskuy.api.model.Movie;
import com.bioskuy.api.model.ShowingSchedule;
import com.bioskuy.api.model.Theater;
import com.bioskuy.api.repository.ScheduleRepository;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<ShowingSchedule> getAllSchedule(){
        return scheduleRepository.findAll();
    }

    public List<ShowingSchedule> getAllSchedulebyMovie(Movie movie){
        return scheduleRepository.findScheduleByMovie(movie);
    }

    public List<ShowingSchedule> getAllSchedulebyTheater(Theater theater){
        return scheduleRepository.findScheduleByTheater(theater);
    }

}
