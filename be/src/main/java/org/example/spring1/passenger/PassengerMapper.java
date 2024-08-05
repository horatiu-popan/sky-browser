package org.example.spring1.passenger;

import org.example.spring1.booking.BookingMapper;
import org.example.spring1.booking.model.Booking;
import org.example.spring1.booking.model.BookingDTO;
import org.example.spring1.passenger.model.Passenger;
import org.example.spring1.passenger.model.PassengerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PassengerMapper {
    PassengerDTO fromEntity(Passenger passenger);
}
