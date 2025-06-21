package com.bioskuy.api.service;

import com.bioskuy.api.entity.Booking;
import com.bioskuy.api.model.midtrans.SnapTransactionResult;
import com.bioskuy.api.model.payment.PaymentRequest;
import com.midtrans.httpclient.error.MidtransError;

import java.io.IOException;

public interface PaymentServiceInterface {

    SnapTransactionResult processPayment(Booking booking) throws MidtransError;

    void verifyPayment(PaymentRequest request) throws IOException;
}
