package org.example.spring1.booking;

import org.example.spring1.booking.model.Booking;
import org.example.spring1.booking.model.BookingDTO;
import org.example.spring1.ticket.TicketMapper;
import org.example.spring1.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TicketMapper.class, UserMapper.class})
public interface BookingMapper {

    @Mapping(source = "ticket", target = "ticketDTO")
    @Mapping(source = "user", target = "userDTO")
    BookingDTO fromEntity(Booking booking);
}
