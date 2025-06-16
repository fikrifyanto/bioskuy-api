package com.bioskuy.api.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @Column(name = "ticket_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking_id;

    @OneToOne
    @JoinColumn(name = "seat_id",nullable = false, unique = true)
    private Seat seat_id;

    @Column(name = "unique_code", nullable = false)
    private String uniqueCode;

    public Ticket(Booking booking, Seat seat, String uniqueCode){
        this.booking_id = booking;
        this.seat_id = seat;
        this.uniqueCode = uniqueCode;
    }
}
