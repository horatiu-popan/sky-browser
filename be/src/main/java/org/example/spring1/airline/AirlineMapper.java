package org.example.spring1.airline;

import org.example.spring1.airline.model.Airline;
import org.example.spring1.airline.model.AirlineDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AirlineMapper {
    AirlineDTO fromEntity(Airline airline);
    Airline toEntity(AirlineDTO airlineDTO);
}
