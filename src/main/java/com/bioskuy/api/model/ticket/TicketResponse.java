package com.bioskuy.api.model.ticket;

import com.bioskuy.api.entity.Booking;
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
}
