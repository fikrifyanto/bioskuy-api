package com.bioskuy.api.service;

import com.bioskuy.api.model.seat.SeatResponse;
import java.util.List;

public interface SeatServiceInterface {
    List<SeatResponse> getSeatsByScheduleId(Long id);
}
