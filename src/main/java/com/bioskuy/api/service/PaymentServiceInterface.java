package com.bioskuy.api.service;

import com.bioskuy.api.entity.Booking;
import com.bioskuy.api.model.midtrans.SnapTransactionResult;
import com.midtrans.httpclient.error.MidtransError;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface PaymentServiceInterface {

    SnapTransactionResult processPayment(Booking booking) throws MidtransError;

    void verifyPayment(HttpServletRequest request) throws IOException;
}
