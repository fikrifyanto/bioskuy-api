package com.bioskuy.api.controller;

import com.bioskuy.api.common.ApiResponse;
import com.bioskuy.api.common.ResponseUtil;
import com.bioskuy.api.model.seat.SeatResponse;
import com.bioskuy.api.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;

    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<ApiResponse<List<SeatResponse>>> getSeatsByScheduleId(@PathVariable Long scheduleId) {
        List<SeatResponse> seats = seatService.getSeatsByScheduleId(scheduleId);
        return ResponseEntity.ok(ResponseUtil.success("Seats fetched successfully", seats));
    }
}
