package com.bioskuy.api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bioskuy.api.common.ApiResponse;
import com.bioskuy.api.common.ResponseUtil;
import com.bioskuy.api.enums.PaymentStatus;
import com.bioskuy.api.model.Booking;
import com.bioskuy.api.model.Seat;
import com.bioskuy.api.model.Schedule;
import com.bioskuy.api.model.User;
import com.bioskuy.api.service.BookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;
    
    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Booking>>> getBookings(){
        List<Booking> bookings = bookingService.getAllBooking();
        return ResponseEntity.ok(ResponseUtil.success("Retreived all " + bookings.size()+" booking(s)", bookings));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Booking>> createBooking(User user, Schedule schedule, List<Seat> selectedSeats){
        try {
            Booking booking = new Booking();
            LocalDateTime bookingDateTime = LocalDateTime.now();
            
            booking.setBookingId(null);
            booking.setBookingDateTime(bookingDateTime);
            booking.setSchedule(schedule);
            booking.setSelectedSeats(selectedSeats);
            booking.setStatus(PaymentStatus.AWAITING_CONFIRMATION);
            booking.setUser(user);
            booking.setTotalPrice(schedule.getTicketPrice());

            return ResponseEntity.ok(ResponseUtil.success("Booking created successfully", booking));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.error("Invalid input: " + e.getMessage()));
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Booking>> getBookingbyId(@PathVariable Long id){
       try {
            Booking booking = bookingService.getBookingbyId(id);
            return ResponseEntity.ok(ResponseUtil.success("Found Booking with id " + id, booking));
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
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBookingbyId(@PathVariable Long id) {
    try {
        bookingService.deleteBookingById(id);
        return ResponseEntity.ok(ResponseUtil.success("Booking cancelled successfully", null));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtil.error("Failed to cancel booking: " + e.getMessage()));
    }
}
}
