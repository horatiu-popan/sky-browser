package org.example.spring1.ticket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDTO {
    private Long originAirportId;
    private Long destinationAirportId;
    private double price;
    private Set<Long> flightIds;
}

