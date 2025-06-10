package com.bioskuy.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.enums.PaymentStatus;
import com.bioskuy.api.model.Booking;
import com.bioskuy.api.model.ShowingSchedule;
import com.bioskuy.api.model.User;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * @param user
     * @return List of booking done by user if exist, empty list otherwise
     */
    List<Booking> findByUser(User user);

    /**
     * @param id
     * @return Optional booking if exist, empty otherwise
     */
    Optional<Booking> findByBookingId(Long id);

    /**
     * @param schedule 
     * @return List of booking in a schedule if exist, empty list otherwise
     */
    List<Booking> findBySchedule(ShowingSchedule schedule);

    /**
     * @param paymentStatus 
     * @return List of boooking by payment status if exist, empty list otherwise
     */
    List<Booking> findByPaymentStatus(PaymentStatus paymentStatus);

}