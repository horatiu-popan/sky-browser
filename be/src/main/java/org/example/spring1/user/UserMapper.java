package org.example.spring1.user;

import org.example.spring1.airport.model.Airport;
import org.example.spring1.airport.model.AirportDTO;
import org.example.spring1.city.CityMapper;
import org.example.spring1.user.model.User;
import org.example.spring1.user.model.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO fromEntity(User user);
    User toEntity(UserDTO userDTO);
}
