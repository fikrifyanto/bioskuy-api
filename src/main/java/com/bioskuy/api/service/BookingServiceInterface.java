package com.bioskuy.api.service;

import com.bioskuy.api.model.booking.BookingRequest;
import com.bioskuy.api.model.booking.BookingResponse;
import com.bioskuy.api.model.payment.PaymentResponse;
import com.midtrans.httpclient.error.MidtransError;

public interface BookingServiceInterface {
    BookingResponse getBookingbyId(Long id);

    PaymentResponse createBooking(BookingRequest bookingRequest) throws MidtransError;

    BookingResponse cancelBooking(Long id);
}

