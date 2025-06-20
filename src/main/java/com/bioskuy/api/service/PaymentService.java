package com.bioskuy.api.service;

import com.bioskuy.api.entity.Booking;
import com.bioskuy.api.entity.Payment;
import com.bioskuy.api.enums.BookingStatus;
import com.bioskuy.api.enums.PaymentStatus;
import com.bioskuy.api.model.midtrans.SnapTransactionResult;
import com.bioskuy.api.repository.BookingRepository;
import com.bioskuy.api.repository.PaymentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midtrans.httpclient.error.MidtransError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.MessageDigest;

@Service
public class PaymentService implements PaymentServiceInterface {

    private final MidtransService midtransService;

    private final PaymentRepository paymentRepository;

    private final BookingRepository bookingRepository;

    private final ObjectMapper objectMapper;

    @Value("${midtrans.server-key}")
    private String serverKey;

    public PaymentService(MidtransService midtransService, PaymentRepository paymentRepository, BookingRepository bookingRepository, ObjectMapper objectMapper) {
        this.midtransService = midtransService;
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.objectMapper = objectMapper;
    }

    public SnapTransactionResult processPayment(Booking booking) throws MidtransError {
        SnapTransactionResult snapTransactionResult = midtransService.createSnapTransaction(
                booking.getId().toString(),
                booking.getAmount(),
                booking.getCustomer().getName(),
                booking.getCustomer().getEmail()
        );

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setToken(snapTransactionResult.getToken());
        payment.setAmount(booking.getAmount());
        payment.setStatus(PaymentStatus.PENDING);

        this.paymentRepository.save(payment);

        return snapTransactionResult;
    }

    public void verifyPayment(HttpServletRequest request) throws IOException  {

        JsonNode payload = objectMapper.readTree(request.getInputStream());

        String orderId = payload.get("order_id").asText();
        String statusCode = payload.get("status_code").asText();
        String grossAmount = payload.get("gross_amount").asText();
        String signatureKey = payload.get("signature_key").asText();
        String transactionStatus = payload.get("transaction_status").asText();

        String dataToHash = orderId + statusCode + grossAmount + serverKey;
        String generatedSignature = sha512(dataToHash);

        if (!generatedSignature.equals(signatureKey)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Signature");
        }

        Booking booking = bookingRepository.findById(Long.valueOf(orderId))
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        switch (transactionStatus) {
            case "settlement", "capture" -> booking.setStatus(BookingStatus.PAID);
            case "pending" -> booking.setStatus(BookingStatus.PENDING);
            case "deny", "cancel", "expire", "failure" -> booking.setStatus(BookingStatus.CANCELLED);
        }

        bookingRepository.save(booking);

        switch (transactionStatus) {
            case "settlement", "capture" -> booking.getPayment().setStatus(PaymentStatus.PAID);
            case "pending" -> booking.getPayment().setStatus(PaymentStatus.PENDING);
            case "deny", "cancel", "expire", "failure" -> booking.getPayment().setStatus(PaymentStatus.CANCELLED);
        }

        paymentRepository.save(booking.getPayment());
    }


    private String sha512(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA-512 hashing failed", e);
        }
    }
}
