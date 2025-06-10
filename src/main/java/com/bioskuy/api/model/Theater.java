package com.bioskuy.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "theaters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theater_id;

    @Column(name = "theater_name", nullable = false)
    private String theater_name;

    @Column(name = "location", nullable = false)
    private String location;

    public Theater(String theater_name, String location){
        this.theater_name = theater_name;
        this.location = location;
    }
}
