package org.example.spring1.flight.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.spring1.airline.model.AirlineDTO;
import org.example.spring1.airport.model.AirportDTO;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {
    private Long id;
    private AirlineDTO airlineDTO;
    private String number;
    private AirportDTO originAirportDTO;
    private AirportDTO destinationAirportDTO;
    private OffsetDateTime departureTime;
    private OffsetDateTime arrivalTime;
}
