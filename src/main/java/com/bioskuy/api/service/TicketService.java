package com.bioskuy.api.service;

import java.util.List;
import java.util.UUID;

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

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> getAllTickets(){
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets;
    }

    public Ticket getTicketbyId(Long id){
        Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Ticket not found"));
        return ticket;
    }

    public Ticket createTicket(Booking booking) {
        Ticket ticket = new Ticket();
        ticket.setBooking(booking);
        return ticketRepository.save(ticket);
    }

    private String generateUniqueCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
