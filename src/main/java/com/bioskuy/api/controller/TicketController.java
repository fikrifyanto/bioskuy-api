package com.bioskuy.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bioskuy.api.common.ApiResponse;
import com.bioskuy.api.common.ResponseUtil;
import com.bioskuy.api.entity.Ticket;
import com.bioskuy.api.service.TicketService;

@RestController
@CrossOrigin
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Ticket>>> getTickets(){
        List<Ticket> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(ResponseUtil.success("Retreive all " + tickets.size() + " ticket(s)", tickets));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Ticket>> getTicketbyId(@PathVariable Long id){
        try {
            Ticket ticket = ticketService.getTicketbyId(id);
            return ResponseEntity.ok(ResponseUtil.success("Found Ticket with id " + id, ticket));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.error(e.getMessage()));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.error("Invalid input: " + e.getMessage()));
            }
        }
    }
}
