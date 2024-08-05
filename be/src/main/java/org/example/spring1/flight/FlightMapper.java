package org.example.spring1.flight;

import org.example.spring1.airline.AirlineMapper;
import org.example.spring1.airport.AirportMapper;
import org.example.spring1.flight.model.Flight;
import org.example.spring1.flight.model.FlightDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {AirlineMapper.class, AirportMapper.class})
@Component
public interface FlightMapper {
    @Mapping(source = "airline", target = "airlineDTO")
    @Mapping(source = "origin", target = "originAirportDTO")
    @Mapping(source = "destination", target = "destinationAirportDTO")
    FlightDTO fromEntity(Flight flight);
    @Mapping(source = "airlineDTO", target = "airline")
    @Mapping(source = "originAirportDTO", target = "origin")
    @Mapping(source = "destinationAirportDTO", target = "destination")
    Flight toEntity(FlightDTO flightDTO);
}
