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

@RestController
@CrossOrigin
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * @param bookingRequest The booking request DTO containing scheduleId and selected seat IDs
     * @return ResponseEntity with ApiResponse containing the created Booking as a DTO
     */
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

    /**
     * @param id The unique identifier of the booking to retrieve
     * @return ResponseEntity with ApiResponse containing the BookingResponseDTO with certain id
     */
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

    /**
     * @param id The unique identifier of the booking to cancel
     * @return {@code 200 OK} with the updated booking if the booking was successfully cancelled,
     *         {@code 409 Conflict} if the booking has already been paid,
     *         or {@code 404 Not Found} if the booking does not exist
     */
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
