package com.bioskuy.api.model.ticket;

import com.bioskuy.api.entity.Booking;
import com.bioskuy.api.entity.BookingSeat;
import com.bioskuy.api.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Long id;

    private String ticketNumber;

    private Booking booking;

    private Seat seat;

    private BookingSeat bookingSeat;
}
