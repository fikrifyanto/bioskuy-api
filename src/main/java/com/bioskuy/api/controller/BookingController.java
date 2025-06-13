package com.bioskuy.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bioskuy.api.common.ApiResponse;
import com.bioskuy.api.common.ResponseUtil;
import com.bioskuy.api.enums.PaymentStatus;
import com.bioskuy.api.model.Booking;
import com.bioskuy.api.model.Seat;
import com.bioskuy.api.security.JwtUtil;
import com.bioskuy.api.service.BookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    
    private final BookingService bookingService;

    /**
     * @param bookingService
     * @param jwtUtil
     */
    @Autowired
    public BookingController(BookingService bookingService, JwtUtil jwtUtil) {
        this.bookingService = bookingService;
    }

    /**
     * @param booking
     * @return ResponseEntity with ApiResponse containing the created Booking
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Booking>> createBooking(@RequestBody Booking booking){
        List<Seat> seats = booking.getSelectedSeats();
        
        //Return list of taken seats if exists, empty list otherwise
        List<Seat> TakenSeats = bookingService.SeatTaken(seats);
        
        //Turn list of taken seats to a string
        String listOfSeats = TakenSeats.stream().map(Seat::getSeatNumber).collect(Collectors.joining(", "));
        
        //Check if there are taken seats
        if(!TakenSeats.isEmpty()){
            throw new IllegalArgumentException("Seat(s) "+listOfSeats+" is unavailable, booking failed");
        }
        
        Booking createdBooking = bookingService.createBooking(booking);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ResponseUtil.success("Booking created successfully", createdBooking));
    }

    /**
     * @param id
     * @return ResponseEntity with ApiResponse containing the Booking with certain id
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Booking>> viewBookingDetails(@PathVariable Long id){
        try {
            Booking booking = bookingService.getBookingbyId(id);
            return ResponseEntity.ok(ResponseUtil.success("Booking found", booking));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.error("Booking not found: " + e.getMessage()));
        }
    }
    
    /**
     * @param id
     * @return {@code 204 No Content} if the booking was successfully deleted,
    *         {@code 409 Conflict} if the booking has already been paid,
    *         or {@code 404 Not Found} if the booking does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id){
        try{
            Booking booking = bookingService.getBookingbyId(id);

            PaymentStatus status = booking.getPaymentStatus();
            if(status == PaymentStatus.PAID){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Booking is paid, can't delete");
            }

            bookingService.deleteBooking(id);
            return ResponseEntity.noContent().build();
            
        }catch(IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking doesn't exist, can't delete");
        }
    }
}
