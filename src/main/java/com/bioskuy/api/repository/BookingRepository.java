package com.bioskuy.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.enums.PaymentStatus;
import com.bioskuy.api.model.Booking;
import com.bioskuy.api.model.Schedule;
import com.bioskuy.api.model.User;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    void deleteByBookingId(Long id);
    /**
     * @param user The user whose bookings are to be retrieved
     * @return List of booking done by user if existed, empty list otherwise
     */
    List<Booking> findByUser(User user);

    /**
     * @param id The booking ID to search for
     * @return Optional booking if existed, empty otherwise
     */
    Optional<Booking> findByBookingId(Long id);

    /**
     * @param schedule The schedule for which to retrieve bookings
     * @return List of booking in a schedule if existed, empty list otherwise
     */
    List<Booking> findBySchedule(Schedule schedule);

    /**
     * @param paymentStatus The payment status to filter bookings by
     * @return List of booking by payment status if existed, empty list otherwise
     */
    List<Booking> findByPaymentStatus(PaymentStatus paymentStatus);

}
