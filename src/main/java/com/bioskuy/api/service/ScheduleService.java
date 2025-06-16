package com.bioskuy.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bioskuy.api.model.Schedule;
import com.bioskuy.api.model.Theater;
import com.bioskuy.api.repository.ScheduleRepository;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * Get all schedules with pagination
     * 
     * @param pageable pagination information
     * @return Page of schedules
     */
    public Page<Schedule> getAllSchedule(Pageable pageable){
        return scheduleRepository.findAll(pageable);
    }

    /**
     * Get all schedules for a specific theater with pagination
     * 
     * @param theater the theater to get schedules for
     * @param pageable pagination information
     * @return Page of schedules for the specified theater
     */
    public Page<Schedule> getAllSchedulebyTheater(Theater theater, Pageable pageable){
        return scheduleRepository.findScheduleByTheater(theater, pageable);
    }

}
