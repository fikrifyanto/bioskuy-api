package com.bioskuy.api.model.booking;

import java.util.List;

import com.bioskuy.api.entity.*;
import com.bioskuy.api.enums.BookingStatus;
import com.bioskuy.api.model.bookingSeats.BookingSeatResponse;
import com.bioskuy.api.model.customer.CustomerResponse;
import com.bioskuy.api.model.schedule.ScheduleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for booking responses.
 * This class is used to return booking data to clients.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private Long id;

    private Double amount;

    private BookingStatus status;

    private CustomerResponse customer;

    private ScheduleResponse schedule;

    private List<BookingSeatResponse> bookingSeats;

    private List<Ticket> tickets;
}
