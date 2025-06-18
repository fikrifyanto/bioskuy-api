package com.bioskuy.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.entity.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
