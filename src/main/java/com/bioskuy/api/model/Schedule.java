package com.bioskuy.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    @JsonBackReference
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    @JsonBackReference
    private Theater theater;

    @Column(name = "showing_date", nullable = false)
    private LocalDate showingDate;

    @Column(name = "showing_time", nullable = false)
    private LocalTime showingTime;

    @Column(name = "ticket_price", nullable = false)
    private double ticketPrice;

    public Schedule(Movie movie, Theater theater, LocalDate showingDate, LocalTime showingTime, double ticketPrice){
        this.movie = movie;
        this.theater = theater;
        this.showingDate = showingDate;
        this.showingTime = showingTime;
        this.ticketPrice = ticketPrice;
    }
}