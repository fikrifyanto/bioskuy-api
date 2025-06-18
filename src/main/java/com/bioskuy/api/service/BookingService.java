package com.bioskuy.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bioskuy.api.model.Booking;
import com.bioskuy.api.repository.BookingRepository;
import com.bioskuy.api.repository.ScheduleRepository;
import com.bioskuy.api.repository.SeatRepository;
import com.bioskuy.api.repository.UserRepository;

@Service
public class BookingService {
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    
    @Autowired
    public BookingService(
        UserRepository userRepository,
        ScheduleRepository scheduleRepository, 
        SeatRepository seatRepository, 
        BookingRepository bookingRepository
    )     
    {
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllBooking(){
        return bookingRepository.findAll();
    }

    public Booking getBookingbyId(Long id){
        Booking booking = bookingRepository.findByBookingId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        return booking;
    }

    public void deleteBookingById(Long id){
        bookingRepository.deleteByBookingId(id);
    }
    
    
}
