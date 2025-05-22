package com.bioskuy.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.model.Booking;
import com.bioskuy.api.model.User;
import com.bioskuy.api.model.enums.PaymentStatus;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUser(User user);

    List<Booking> findBySchedule(Long scheduleId);

    List<Booking> findByPaymentStatus(PaymentStatus paymentStatus);
}