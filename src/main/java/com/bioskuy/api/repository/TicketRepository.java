package com.bioskuy.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bioskuy.api.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * @param ticket_id Movie id
     * @return Optional containing Movie based on id if exist, empty otherwise
     */
    Optional<Ticket> findTicketByTicketId(Long ticket_id);

    /**
     * @param uniqueCode
     * @return Optional Ticket if exists, empty otherwise
     */
    Optional<Ticket> findByUniqueCode(String uniqueCode);
}
