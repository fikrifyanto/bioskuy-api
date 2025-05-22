package com.bioskuy.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.enums.PaymentStatus;
import com.bioskuy.api.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * @param userId User Id
     * @return List of booking done by user if exist, empty list otherwise
     */
    List<Booking> findByUser(Long userId);

    /**
     * @param scheduleId Schedule Id
     * @return List of booking in a schedule if exist, empty list otherwise
     */
    List<Booking> findBySchedule(Long scheduleId);

    /**
     * @param paymentStatus Enums PaymentStatus (see model/enums.java)
     * @return List of boooking by payment status if exist, empty list otherwise
     */
    List<Booking> findByPaymentStatus(PaymentStatus paymentStatus);
}