package com.bioskuy.api.service;

import com.bioskuy.api.entity.Schedule;
import com.bioskuy.api.model.schedule.ScheduleResponse;
import org.springframework.data.domain.Page;

public interface ScheduleServiceInterface {

    Page<ScheduleResponse> getAllSchedules(
            Long theaterId,
            Long movieId,
            int page,
            int size,
            String sortBy,
            String direction
    );

    ScheduleResponse toScheduleResponse(Schedule schedule);
}
