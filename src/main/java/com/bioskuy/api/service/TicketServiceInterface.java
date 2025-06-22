package com.bioskuy.api.service;

import com.bioskuy.api.model.ticket.TicketBookingResponse;

import java.util.List;

public interface TicketServiceInterface {

    List<TicketBookingResponse> getTicketsByBookingId(Long bookingId);

}
