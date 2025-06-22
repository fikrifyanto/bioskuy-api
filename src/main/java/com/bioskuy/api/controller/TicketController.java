package com.bioskuy.api.controller;

import java.util.List;

import com.bioskuy.api.model.ticket.TicketBookingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bioskuy.api.common.ApiResponse;
import com.bioskuy.api.common.ResponseUtil;
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

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<List<TicketBookingResponse>>> getTicketsByBookingId(@PathVariable Long bookingId) {
        List<TicketBookingResponse> tickets = ticketService.getTicketsByBookingId(bookingId);

        return ResponseEntity.ok(ResponseUtil.success("Tickets retrieved successfully", tickets));
    }
}
