package com.bioskuy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bioskuy.api.common.ApiResponse;
import com.bioskuy.api.common.ResponseUtil;
import com.bioskuy.api.dto.ScheduleDTO;
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

    /**
     * Get all schedules with optional pagination
     *
     * @param page      Page number (0-based)
     * @param size      Page size
     * @param sortBy    Field to sort by (default: "id")
     * @param direction Sort direction (default: "asc")
     * @return ResponseEntity with ApiResponse containing the page of schedules
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllSchedule(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Schedule> schedulePage = scheduleService.getAllSchedule(pageable);

        Page<ScheduleDTO> scheduleDTOPage = schedulePage.map(ScheduleDTO::fromSchedule);

        return ResponseEntity.ok(ResponseUtil.success(
                "Retrieved " + scheduleDTOPage.getNumberOfElements() + " Schedule(s) (Page " + (page + 1) + " of " + scheduleDTOPage.getTotalPages() + ")",
                scheduleDTOPage));
    }

    /**
     * Get schedules by theater ID with optional pagination
     *
     * @param id        Theater ID
     * @param page      Page number (0-based)
     * @param size      Page size
     * @param sortBy    Field to sort by (default: "id")
     * @param direction Sort direction (default: "asc")
     * @return ResponseEntity with ApiResponse containing the page of schedules
     */
    @GetMapping("/theater/{id}")
    public ResponseEntity<ApiResponse<?>> getSchedulesByTheaterId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        try {
            Theater theater = theaterService.getTheaterbyId(id);

            Sort sort = direction.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() :
                    Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Schedule> schedulePage = scheduleService.getAllSchedulebyTheater(theater, pageable);

            Page<ScheduleDTO> scheduleDTOPage = schedulePage.map(ScheduleDTO::fromSchedule);

            return ResponseEntity.ok(ResponseUtil.success(
                    "Retrieved " + scheduleDTOPage.getNumberOfElements() + " Schedule(s) From Theater " + theater.getName() + 
                    " (Page " + (page + 1) + " of " + scheduleDTOPage.getTotalPages() + ")",
                    scheduleDTOPage));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.error(e.getMessage()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.error("Invalid input: " + e.getMessage()));
            }
        }
    }

}
