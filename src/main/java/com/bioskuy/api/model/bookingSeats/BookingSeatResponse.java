package com.bioskuy.api.model.bookingSeats;

import com.bioskuy.api.model.seat.SeatResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSeatResponse {

    private Long id;

    private SeatResponse seat;
}
