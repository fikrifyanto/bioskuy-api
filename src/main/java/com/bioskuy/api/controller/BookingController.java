package com.bioskuy.api.controller;

import com.bioskuy.api.model.payment.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.bioskuy.api.common.ApiResponse;
import com.bioskuy.api.common.ResponseUtil;
import com.bioskuy.api.model.booking.BookingRequest;
import com.bioskuy.api.model.booking.BookingResponse;
import com.bioskuy.api.service.BookingService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAllBookings() {
        try {
            List<BookingResponse> bookings = bookingService.getAllBookings();
            return ResponseEntity.ok(ResponseUtil.success("Bookings retrieved successfully", bookings));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.error("Failed to retrieve bookings: " + e.getMessage()));
        }
    }


    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> createBooking(@RequestBody BookingRequest bookingRequest) {
        try {
            PaymentResponse createdBooking = bookingService.createBooking(bookingRequest);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtil.success("Booking created successfully", createdBooking));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.error("Failed to create booking: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> showBooking(@PathVariable Long id) {
        try {
            BookingResponse booking = bookingService.getBookingbyId(id);
            return ResponseEntity.ok(ResponseUtil.success("Booking found", booking));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.error("Booking not found: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(@PathVariable Long id) {
        try {
            BookingResponse cancelledBooking = bookingService.cancelBooking(id);

            return ResponseEntity.ok(ResponseUtil.success("Booking cancelled successfully", cancelledBooking));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking doesn't exist, can't cancel");
        }
    }
}
