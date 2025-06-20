package com.bioskuy.api.model.booking;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for booking requests.
 * This class is used to receive booking data from clients.
 * Contains scheduleId, seatIds, and userId.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    @NotNull
    private Long scheduleId;

    @NotNull
    private List<Long> seatIds;
}
