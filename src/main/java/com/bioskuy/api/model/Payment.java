package com.bioskuy.api.model;

import com.bioskuy.api.enums.PaymentStatus;   // ← enum di package berbeda
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    @JsonManagedReference
    private Booking booking;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "payment_datetime")
    private LocalDateTime paymentDateTime;

    @Column(name = "amount_paid")
    private double amountPaid;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public void processPayment() {
        // Example business logic for payment processing
        if (status == PaymentStatus.PENDING) {
            status = PaymentStatus.AWAITING_CONFIRMATION;
            System.out.println("Payment is being processed for booking ID: " + booking.getBookingId());
        }
    }

    public boolean verifyPayment() {
        // Example verification logic
        return this.status == PaymentStatus.PAID;
    }
}

