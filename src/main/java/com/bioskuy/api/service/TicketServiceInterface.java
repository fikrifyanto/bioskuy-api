package com.bioskuy.api.service;

import com.bioskuy.api.model.ticket.TicketResponse;

import java.util.List;

public interface TicketServiceInterface {

    List<TicketResponse> getTicketsByBookingId(Long bookingId);

}
