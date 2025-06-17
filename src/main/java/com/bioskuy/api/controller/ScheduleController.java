package com.bioskuy.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bioskuy.api.common.ApiResponse;
import com.bioskuy.api.common.ResponseUtil;
import com.bioskuy.api.model.Schedule;
import com.bioskuy.api.model.Theater;
import com.bioskuy.api.service.ScheduleService;
import com.bioskuy.api.service.TheaterService;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final TheaterService theaterService;
    private final ScheduleService scheduleService;
    
    @Autowired
    public ScheduleController(TheaterService theaterService,ScheduleService scheduleService) {
        this.theaterService = theaterService;
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Schedule>>> getAllSchedule(){
        List<Schedule> schedules = scheduleService.getAllSchedule();
        return ResponseEntity.ok(ResponseUtil.success("Retreived " + schedules.size() + " Schedule(s)", schedules));
    }

    @GetMapping("/theater/{id}")
    public ResponseEntity<ApiResponse<List<Schedule>>> getSchedulesbyTheaterId(@PathVariable Long id){
        try {
            Theater theater = theaterService.getTheaterbyId(id);
            List<Schedule> schedulesTheater = scheduleService.getAllSchedulebyTheater(theater);
            return ResponseEntity.ok(ResponseUtil.success("Retreived " + schedulesTheater.size() + " Schedule(s) From Theater " + theater.getTheater_name(), schedulesTheater));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.error(e.getMessage()));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.error("Invalid input: " + e.getMessage()));
            }
        }
    }

}
