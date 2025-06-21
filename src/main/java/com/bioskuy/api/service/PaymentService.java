package com.bioskuy.api.service;

import com.bioskuy.api.entity.Booking;
import com.bioskuy.api.entity.Payment;
import com.bioskuy.api.entity.Seat;
import com.bioskuy.api.entity.Ticket;
import com.bioskuy.api.enums.BookingStatus;
import com.bioskuy.api.enums.PaymentStatus;
import com.bioskuy.api.enums.SeatStatus;
import com.bioskuy.api.model.midtrans.SnapTransactionResult;
import com.bioskuy.api.model.payment.PaymentRequest;
import com.bioskuy.api.repository.BookingRepository;
import com.bioskuy.api.repository.PaymentRepository;
import com.bioskuy.api.repository.SeatRepository;
import com.bioskuy.api.repository.TicketRepository;
import com.midtrans.httpclient.error.MidtransError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class PaymentService implements PaymentServiceInterface {

    private final MidtransService midtransService;

    private final PaymentRepository paymentRepository;

    private final BookingRepository bookingRepository;

    private final TicketRepository ticketRepository;

    private final SeatRepository seatRepository;

    private final AtomicInteger counter = new AtomicInteger(0);

    @Value("${midtrans.server-key}")
    private String serverKey;

    public PaymentService(
            MidtransService midtransService,
            PaymentRepository paymentRepository,
            BookingRepository bookingRepository,
            TicketRepository ticketRepository,
            SeatRepository seatRepository
    ) {
        this.midtransService = midtransService;
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
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

    public void verifyPayment(PaymentRequest request) {

        String dataToHash = request.getOrderId() + request.getStatusCode() + request.getGrossAmount() + serverKey;
        String generatedSignature = generateSignature(dataToHash);

        if (!generatedSignature.equals(request.getSignatureKey())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Signature");
        }

        Booking booking = bookingRepository.findById(Long.valueOf(request.getOrderId()))
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        switch (request.getTransactionStatus()) {
            case "settlement", "capture" -> booking.setStatus(BookingStatus.PAID);
            case "pending" -> booking.setStatus(BookingStatus.PENDING);
            case "deny", "cancel", "expire", "failure" -> booking.setStatus(BookingStatus.CANCELLED);
        }

        bookingRepository.save(booking);

        switch (request.getTransactionStatus()) {
            case "settlement", "capture" -> booking.getPayment().setStatus(PaymentStatus.PAID);
            case "pending" -> booking.getPayment().setStatus(PaymentStatus.PENDING);
            case "deny", "cancel", "expire", "failure" -> booking.getPayment().setStatus(PaymentStatus.CANCELLED);
        }

        paymentRepository.save(booking.getPayment());

        if (request.getTransactionStatus().equals("settlement") || request.getTransactionStatus().equals("capture")) {
            booking.getBookingSeats().forEach(bs -> {
                Ticket ticket = new Ticket();
                ticket.setTicketNumber(generateTicketNumber());
                ticket.setBooking(booking);
                ticket.setSeat(bs.getSeat());
                ticket.setBookingSeat(bs);

                ticketRepository.save(ticket);
            });
        }

        if (request.getTransactionStatus().equals("deny") ||
                request.getTransactionStatus().equals("cancel") ||
                request.getTransactionStatus().equals("expire") ||
                request.getTransactionStatus().equals("failure")) {
            booking.getBookingSeats().forEach(bs -> {
                Seat seat = bs.getSeat();
                seat.setStatus(SeatStatus.AVAILABLE);

                seatRepository.save(seat);
            });
        }
    }


    public String generateSignature(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();

            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b)); // Convert to hex
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 algorithm not available", e);
        }
    }

    public String generateTicketNumber() {
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int sequence = counter.incrementAndGet();
        String paddedSequence = String.format("%05d", sequence);

        return "TICKET-" + datePart + "-" + paddedSequence;
    }
}
