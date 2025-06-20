package com.bioskuy.api.service;

import com.bioskuy.api.entity.Booking;
import com.bioskuy.api.entity.Ticket;

import java.util.List;

public interface TicketServiceInterface {
    List<Ticket> getAllTickets();
    Ticket getTicketbyId(Long id);
    Ticket createTicket(Booking booking);
}
