package com.bioskuy.api.model;
import java.time.LocalDateTime;


public class Payment {

    private Long id;
    private Booking booking;
    private String paymentMethod;
    private LocalDateTime paymentDateTime;
    private double amountPaid;
    private PaymentStatus status;

    // Constructors
    public Payment() {
    }

    public Payment(Long id, Booking booking, String paymentMethod, LocalDateTime paymentDateTime, double amountPaid, PaymentStatus status) {
        this.id = id;
        this.booking = booking;
        this.paymentMethod = paymentMethod;
        this.paymentDateTime = paymentDateTime;
        this.amountPaid = amountPaid;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(LocalDateTime paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    // Methods
    public void processPayment() {
        this.status = PaymentStatus.PROCESSED;
        this.paymentDateTime = LocalDateTime.now();
    }

    public void verifyPayment() {
        if (this.status == PaymentStatus.PROCESSED) {
            this.status = PaymentStatus.VERIFIED;
        }
    }
}
