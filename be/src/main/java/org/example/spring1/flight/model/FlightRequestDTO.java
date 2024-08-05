package org.example.spring1.flight.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightRequestDTO {
    private Long airlineId;
    private String number;
    private Long originAirportId;
    private Long destinationAirportId;
    private OffsetDateTime departureTime;
    private OffsetDateTime arrivalTime;
}
