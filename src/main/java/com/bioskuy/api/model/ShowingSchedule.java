package com.bioskuy.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowingSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schedule_id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "movie", nullable = false)
    private Movie movie;

    @ManyToOne(optional = false)
    @JoinColumn(name = "theater", nullable = false)
    private Theater theater;

    @Column(nullable = false)
    private LocalDate showingDate;

    @Column(nullable = false)
    private LocalTime showingTime;

    @Column(nullable = false)
    private double ticketPrice;
}
