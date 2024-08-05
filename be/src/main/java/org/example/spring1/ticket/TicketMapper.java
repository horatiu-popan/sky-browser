package org.example.spring1.ticket;

import org.example.spring1.airport.AirportMapper;
import org.example.spring1.flight.FlightMapper;
import org.example.spring1.ticket.model.Ticket;
import org.example.spring1.ticket.model.TicketDTO;
import org.example.spring1.ticket.model.TicketRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {AirportMapper.class, FlightMapper.class})
@Component
public interface TicketMapper {

    @Mapping(source = "originAirport", target = "originAirportDTO")
    @Mapping(source = "destinationAirport", target = "destinationAirportDTO")
    @Mapping(source = "flights", target = "flights")
    TicketDTO fromEntity(Ticket ticket);

    @Mapping(source = "originAirportDTO", target = "originAirport")
    @Mapping(source = "destinationAirportDTO", target = "destinationAirport")
    @Mapping(source = "flights", target = "flights")
    Ticket toEntity(TicketDTO ticketDTO);

    @Mapping(source = "originAirport.id", target = "originAirportId")
    @Mapping(source = "destinationAirport.id", target = "destinationAirportId")
    @Mapping(source = "price", target = "price")
    @Mapping(target = "flightIds", expression = "java(ticket.getFlights().stream().map(flight -> flight.getId()).collect(java.util.stream.Collectors.toSet()))")
    TicketRequestDTO toRequestDTO(Ticket ticket);
}
