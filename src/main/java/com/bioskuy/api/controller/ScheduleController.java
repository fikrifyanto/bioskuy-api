package com.bioskuy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bioskuy.api.common.ApiResponse;
import com.bioskuy.api.common.ResponseUtil;
import com.bioskuy.api.model.schedule.ScheduleResponse;
import com.bioskuy.api.service.ScheduleService;

@RestController
@CrossOrigin
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * Get all schedules with optional filters and pagination
     *
     * @param theaterId  Optional filter by theater ID
     * @param movieId    Optional filter by movie ID
     * @param page       Page number (0-based)
     * @param size       Page size
     * @param sortBy     Field to sort by (default: "id")
     * @param direction  Sort direction (asc or desc)
     * @return Paginated schedules
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllSchedules(
            @RequestParam(required = false) Long theaterId,
            @RequestParam(required = false) Long movieId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Page<ScheduleResponse> schedulePage = scheduleService.getAllSchedules(theaterId, movieId, page, size, sortBy, direction);

        return ResponseEntity.ok(ResponseUtil.success(
                "Retrieved " + schedulePage.getNumberOfElements() + " Schedules" +
                        " (Page " + (page + 1) + " of " + schedulePage.getTotalPages() + ")",
                schedulePage));
    }
}
