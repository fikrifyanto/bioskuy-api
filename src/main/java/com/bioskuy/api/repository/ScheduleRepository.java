package com.bioskuy.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.entity.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Page<Schedule> findByTheaterId(Long theaterId, Pageable pageable);

    Page<Schedule> findByMovieId(Long movieId, Pageable pageable);

    Page<Schedule> findByTheaterIdAndMovieId(Long theaterId, Long movieId, Pageable pageable);
}
