package com.bioskuy.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bioskuy.api.model.Theater;
import com.bioskuy.api.repository.TheaterRepository;

@Service
public class TheaterService{
    private final TheaterRepository theaterRepository;

    @Autowired
    public TheaterService(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    public List<Theater> getAllTheater(){
        return theaterRepository.findAll();       
    }

    public Theater getTheaterbyId(Long id){
        Theater theater = theaterRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Theater not found"));

        return theater;
    }
}