package com.bioskuy.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bioskuy.api.enums.PaymentStatus;
import com.bioskuy.api.enums.SeatStatus;
import com.bioskuy.api.model.Booking;
import com.bioskuy.api.model.Seat;
import com.bioskuy.api.model.User;
import com.bioskuy.api.repository.BookingRepository;

@Service
public class BookingService{
    private final BookingRepository bookingRepository;
    
    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking getBookingbyId(Long id){
        Booking booking = bookingRepository.findByBookingId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such Booking"));
        return booking;
    }

    public List<Booking> getBookingsbyUser(User user){
        List<Booking> bookings = bookingRepository.findByUser(user);
        if (bookings.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Booking from User " + user.getName());
        }
        return bookings;
    }

    public Booking createBooking(Booking booking){
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Long id, Booking updatedBooking){
        // check if booking exists
        Booking existBooking = bookingRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Booking not found"));

        // check wether the booking have been paid or cancelled
        PaymentStatus status = existBooking.getPaymentStatus();
        if(status == PaymentStatus.CANCELLED || status == PaymentStatus.PAID){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ticket has been " + status.toString() + ", Can't change booking");
        }

        updatedBooking.setBookingId(id);
        return bookingRepository.save(updatedBooking);
    }

    public List<Seat> SeatTaken(List<Seat> seats){
        SeatStatus status;
        List<Seat> takenSeats = new ArrayList<>();
        // run through every seat in List<Seat>
        for (Seat seat : seats) {
            // get the status of the seat
            status = seat.getStatus();

            // if SeatStatus is RESERVED or SOLD, throw exception
            if (status == SeatStatus.RESERVED || status == SeatStatus.SOLD){
                takenSeats.addLast(seat);
            }                
        }
        return takenSeats;
    }
    
    public void deleteBooking(Long id){
        if (!bookingRepository.existsById(id)) {
            throw new IllegalArgumentException("Booking not found");
        }
        bookingRepository.deleteById(id);
    }
}