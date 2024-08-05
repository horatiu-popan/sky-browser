package org.example.spring1.ticket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.spring1.airport.model.AirportDTO;
import org.example.spring1.flight.model.FlightDTO;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private Long id;
    private AirportDTO originAirportDTO;
    private AirportDTO destinationAirportDTO;
    private double price;
    private Set<FlightDTO> flights;
}

