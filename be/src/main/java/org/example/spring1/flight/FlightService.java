package org.example.spring1.flight;

import lombok.RequiredArgsConstructor;
import org.example.spring1.airline.AirlineRepository;
import org.example.spring1.airline.model.Airline;
import org.example.spring1.airport.AirportRepository;
import org.example.spring1.airport.model.Airport;
import org.example.spring1.exception.EntityNotFoundException;
import org.example.spring1.flight.model.Flight;
import org.example.spring1.flight.model.FlightDTO;
import org.example.spring1.flight.model.FlightRequestDTO;
import org.example.spring1.flight.model.FlightSearchDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.LocalDate;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;
    private final AirportRepository airportRepository;
    private final AirlineRepository airlineRepository;

    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    public ResponseEntity<?> get(Long id) {
        return flightRepository.findById(id)
                .map(flightMapper::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public FlightDTO create(FlightRequestDTO dto) {
        Flight flight = new Flight();
        Airline airline = airlineRepository.findById(dto.getAirlineId())
                .orElseThrow(() -> new EntityNotFoundException("Airline not found with id: " + dto.getAirlineId()));
        flight.setAirline(airline);
        flight.setNumber(dto.getNumber());
        Airport departureAirport = airportRepository.findById(dto.getOriginAirportId())
                .orElseThrow(() -> new EntityNotFoundException("Airport not found with id: " + dto.getOriginAirportId()));
        flight.setOrigin(departureAirport);
        Airport arrivalAirport = airportRepository.findById(dto.getDestinationAirportId())
                .orElseThrow(() -> new EntityNotFoundException("Airport not found with id: " + dto.getDestinationAirportId()));
        flight.setDestination(arrivalAirport);
        flight.setDepartureTime(dto.getDepartureTime());
        flight.setArrivalTime(dto.getArrivalTime());
        Flight savedFlight = flightRepository.save(flight);
        return flightMapper.fromEntity(savedFlight);
    }
    public void delete(Long id) {
        flightRepository.deleteById(id);
    }
    public List<FlightDTO> findAllFiltered(FlightSearchDTO searchDTO) {
        LocalDate departureDate = searchDTO.getDepartureDate();
        OffsetDateTime startOfDay = departureDate.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime endOfDay = departureDate.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC);
        Long originAirportId = airportRepository.findByIataCode(searchDTO.getOriginAirport()).get(0).getId();
        Long destinationAirportId = airportRepository.findByIataCode(searchDTO.getDestinationAirport()).get(0).getId();
        return flightRepository.findByOriginIdAndDestinationIdAndDepartureTimeBetween(
                        originAirportId,
                        destinationAirportId,
                        startOfDay,
                        endOfDay
                ).stream()
                .map(flightMapper::fromEntity)
                .collect(Collectors.toList());
    }

    public FlightDTO update(Long id, FlightRequestDTO dto) {
        Flight existingFlight = flightRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Flight not found with id: " + id));
        Airline airline = airlineRepository.findById(dto.getAirlineId())
                .orElseThrow(() -> new EntityNotFoundException("Airline not found with id: " + dto.getAirlineId()));
        existingFlight.setAirline(airline);
        existingFlight.setNumber(dto.getNumber());
        Airport departureAirport = airportRepository.findById(dto.getOriginAirportId())
                .orElseThrow(() -> new EntityNotFoundException("Airport not found with id: " + dto.getOriginAirportId()));
        existingFlight.setOrigin(departureAirport);
        Airport arrivalAirport = airportRepository.findById(dto.getDestinationAirportId())
                .orElseThrow(() -> new EntityNotFoundException("Airport not found with id: " + dto.getDestinationAirportId()));
        existingFlight.setDestination(arrivalAirport);
        existingFlight.setDepartureTime(dto.getDepartureTime());
        existingFlight.setArrivalTime(dto.getArrivalTime());
        Flight updatedFlight = flightRepository.save(existingFlight);
        return flightMapper.fromEntity(updatedFlight);
    }
}
