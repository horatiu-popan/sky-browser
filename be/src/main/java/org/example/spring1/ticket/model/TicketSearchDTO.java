package org.example.spring1.ticket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketSearchDTO {
    private String originAirport;
    private String destinationAirport;
    private LocalDate departureDate;
}