package org.example.spring1.airport;


import org.example.spring1.airport.model.Airport;
import org.example.spring1.airport.model.AirportDTO;
import org.example.spring1.city.CityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CityMapper.class})
public interface AirportMapper {

    @Mapping(source = "city", target = "cityDTO")
    AirportDTO fromEntity(Airport airport);

    @Mapping(source = "cityDTO", target = "city")
    Airport toDto(AirportDTO airportDTO);
}
