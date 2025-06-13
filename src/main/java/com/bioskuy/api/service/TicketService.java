package com.bioskuy.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bioskuy.api.model.Ticket;
import com.bioskuy.api.repository.TicketRepository;

@Service
public class TicketService {
    
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
        Ticket ticket = ticketRepository.findTicketByTicketId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Movie not found"));
        return ticket;
    }

}
