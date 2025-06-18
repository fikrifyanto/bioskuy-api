package com.bioskuy.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.model.Theater;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {

}
