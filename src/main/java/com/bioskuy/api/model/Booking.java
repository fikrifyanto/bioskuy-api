package com.bioskuy.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

import com.bioskuy.api.enums.PaymentStatus;


@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long booking_id;

    @Column(name = "user", nullable = false)
    private User user;

    @Column(name = "showing_schedule", nullable = false)
    private ShowingSchedule showingSchedule;

    @Column(name = "selected_seats",nullable = false)
    private List<Seat> selectedSeats;

    @Column(name = "booking_date_time", nullable = false)
    private LocalDateTime bookingDateTime;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    // Constructor without id
    public Booking(User user, ShowingSchedule showingSchedule, List<Seat> selectedSeats, LocalDateTime bookingDateTime, double totalPrice, PaymentStatus paymentStatus){
        this.user = user;
        this.showingSchedule = showingSchedule;
        this.selectedSeats = selectedSeats;
        this.bookingDateTime = bookingDateTime;
        this.totalPrice = totalPrice;
        this.paymentStatus = paymentStatus;
    }
}
