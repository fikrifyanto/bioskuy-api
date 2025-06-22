package com.bioskuy.api.service;

import java.util.List;

import com.bioskuy.api.model.ticket.TicketBookingResponse;
import com.bioskuy.api.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bioskuy.api.entity.Booking;
import com.bioskuy.api.entity.Ticket;
import com.bioskuy.api.repository.TicketRepository;

@Service
public class TicketService implements TicketServiceInterface {

    private final TicketRepository ticketRepository;

    private final BookingRepository bookingRepository;

    @Autowired
    public TicketService(
            TicketRepository ticketRepository,
            BookingRepository bookingRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<TicketBookingResponse> getTicketsByBookingId(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        List<Ticket> tickets = ticketRepository.findByBooking(booking);
        if (tickets.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tickets found for booking ID: " + bookingId);
        }

        return tickets.stream()
                .map(this::toTicketBookingResponse)
                .toList();
    }

    public TicketBookingResponse toTicketBookingResponse(Ticket ticket) {
        return TicketBookingResponse.builder()
                .id(ticket.getId())
                .ticketNumber(ticket.getTicketNumber())
                .seat(ticket.getSeat())
                .build();
    }
}
