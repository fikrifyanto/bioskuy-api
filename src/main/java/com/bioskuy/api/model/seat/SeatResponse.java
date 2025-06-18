package com.bioskuy.api.model.seat;

import com.bioskuy.api.enums.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponse {

    private Long id;

    private String seatNumber;

    private SeatStatus status;
}
