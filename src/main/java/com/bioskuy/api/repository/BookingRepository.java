package com.bioskuy.api.repository;

import java.util.List;

import com.bioskuy.api.enums.BookingStatus;
import com.bioskuy.api.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByCustomerAndStatus(Customer customer, BookingStatus status);

}
