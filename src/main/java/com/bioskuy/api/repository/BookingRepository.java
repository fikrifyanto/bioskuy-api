package com.bioskuy.api.repository;

import java.util.List;

import com.bioskuy.api.enums.BookingStatus;
import com.bioskuy.api.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.entity.Booking;
import com.bioskuy.api.entity.Schedule;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * @param customer The user whose bookings are to be retrieved
     * @return List of booking done by user if existed, empty list otherwise
     */
    List<Booking> findByCustomer(Customer customer);

    /**
     * @param schedule The schedule for which to retrieve bookings
     * @return List of booking in a schedule if existed, empty list otherwise
     */
    List<Booking> findBySchedule(Schedule schedule);

    /**
     * @param bookingStatus The payment status to filter bookings by
     * @return List of booking by payment status if existed, empty list otherwise
     */
    List<Booking> findByStatus(BookingStatus bookingStatus);

    void deleteByBookingId(Long id);
}
