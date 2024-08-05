package org.example.spring1.city;

import org.example.spring1.city.model.City;
import org.example.spring1.city.model.CityDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper {

    CityDTO fromEntity(City city);

    City toEntity(CityDTO cityDTO);
}
