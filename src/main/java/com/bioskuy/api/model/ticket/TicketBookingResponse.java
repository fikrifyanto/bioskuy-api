package com.bioskuy.api.model.ticket;

import com.bioskuy.api.model.seat.SeatResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketBookingResponse {
    private Long id;

    private String ticketNumber;

    private SeatResponse seat;
}
