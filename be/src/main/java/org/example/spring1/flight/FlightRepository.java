package org.example.spring1.flight;

import org.example.spring1.flight.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.OffsetDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByOriginIdAndDestinationIdAndDepartureTimeBetween(Long originAirportId,
                                                                       Long destinationAirportId,
                                                                       OffsetDateTime departureTimeStart,
                                                                       OffsetDateTime departureTimeEnd);
}
