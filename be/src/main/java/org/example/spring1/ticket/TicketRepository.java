package org.example.spring1.ticket;

import org.example.spring1.ticket.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByOriginAirportIdAndDestinationAirportId(Long originAirportId,
                                                                       Long destinationAirportId);
}
