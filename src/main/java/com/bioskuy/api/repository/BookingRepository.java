package com.bioskuy.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingId(Long id);
    
    void deleteByBookingId(Long id);
}
