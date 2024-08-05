package org.example.spring1.ticket;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.example.spring1.airline.AirlineRepository;
import org.example.spring1.airline.model.Airline;
import org.example.spring1.airport.AirportRepository;
import org.example.spring1.airport.model.Airport;
import org.example.spring1.flight.FlightRepository;
import org.example.spring1.flight.model.Flight;
import org.example.spring1.ticket.model.Ticket;
import org.example.spring1.ticket.model.TicketDTO;
import org.example.spring1.ticket.model.TicketRequestDTO;
import org.example.spring1.ticket.model.TicketSearchDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.example.spring1.search.GoogleSearch;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final TicketMapper ticketMapper;
    private final AirlineRepository airlineRepository;

    public List<TicketDTO> findAll() {
        return ticketRepository.findAll().stream()
                .map(ticketMapper::fromEntity)
                .collect(Collectors.toList());
    }

    public ResponseEntity<TicketDTO> get(Long id) {
        return ticketRepository.findById(id)
                .map(ticket -> ResponseEntity.ok(ticketMapper.fromEntity(ticket)))
                .orElse(ResponseEntity.notFound().build());
    }

    public TicketDTO create(TicketRequestDTO dto) {
        Ticket ticket = new Ticket();
        ticket.setOriginAirport(airportRepository.findById(dto.getOriginAirportId()).orElseThrow(() -> new RuntimeException("Origin airport not found")));
        ticket.setDestinationAirport(airportRepository.findById(dto.getDestinationAirportId()).orElseThrow(() -> new RuntimeException("Destination airport not found")));
        ticket.setPrice(dto.getPrice());

        Set<Flight> flights = dto.getFlightIds().stream()
                .map(flightId -> flightRepository.findById(flightId).orElseThrow(() -> new RuntimeException("Flight not found")))
                .collect(Collectors.toSet());
        ticket.setFlights(flights);

        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.fromEntity(savedTicket);
    }

    public void delete(Long id) {
        ticketRepository.deleteById(id);
    }

    public List<TicketDTO> findAllFiltered(TicketSearchDTO searchDTO) {
        Map<String, String> parameter = new HashMap<>();

        parameter.put("engine", "google_flights");
        parameter.put("departure_id", searchDTO.getOriginAirport());
        parameter.put("arrival_id", searchDTO.getDestinationAirport());
        parameter.put("outbound_date", searchDTO.getDepartureDate().toString());
//        if (searchDTO.getReturnDate() != null) {
//            parameter.put("return_date", searchDTO.getReturnDate().toString());
//        }
        parameter.put("type", "2"); //type 2 means one-way flight
        parameter.put("currency", "USD");
        parameter.put("hl", "en");

        GoogleSearch search = new GoogleSearch(parameter);
        JsonObject results;
        try {
            results = search.getJson();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<Ticket> apiTickets = parseApiResponse(results);

        LocalDate departureDate = searchDTO.getDepartureDate();
        OffsetDateTime startOfDay = departureDate.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime endOfDay = departureDate.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC);
        Long originAirportId = airportRepository.findByIataCode(searchDTO.getOriginAirport()).get(0).getId();
        Long destinationAirportId = airportRepository.findByIataCode(searchDTO.getDestinationAirport()).get(0).getId();

        List<Ticket> tickets = ticketRepository.findByOriginAirportIdAndDestinationAirportId(
                originAirportId,
                destinationAirportId
        );
        return apiTickets.stream()
                .map(ticketMapper::fromEntity)
                .collect(Collectors.toList());
    }

    private List<Ticket> parseApiResponse(JsonObject results) {
        List<Ticket> tickets = new ArrayList<>();

        JsonArray bestFlights = results.getAsJsonArray("best_flights");
        if (bestFlights == null || bestFlights.size() == 0) {
            bestFlights = results.getAsJsonArray("other_flights");
        }
        for (int i = 0; i < bestFlights.size(); i++) {
            JsonObject flightObj = bestFlights.get(i).getAsJsonObject();

            JsonArray flightsArray = flightObj.getAsJsonArray("flights");

            JsonArray layoversArray = flightObj.getAsJsonArray("layovers");

            JsonObject searchParams = results.getAsJsonObject("search_parameters");

            double price = flightObj.get("price").getAsDouble();

            Ticket ticket = new Ticket();
            ticket.setPrice(price);

            String originAirportCode = searchParams.get("departure_id").getAsString();
            String destinationAirportCode = searchParams.get("arrival_id").getAsString();

            String originAirportName = flightsArray.get(0).getAsJsonObject().getAsJsonObject("departure_airport").get("name").getAsString();
            String destinationAirportName = flightsArray.get(flightsArray.size() - 1).getAsJsonObject().getAsJsonObject("arrival_airport").get("name").getAsString();

            Airport originAirport = findOrCreateAirport(originAirportCode, originAirportName);
            ticket.setOriginAirport(originAirport);

            Airport destinationAirport = findOrCreateAirport(destinationAirportCode, destinationAirportName);
            ticket.setDestinationAirport(destinationAirport);

            Set<Flight> flightSet = new HashSet<>();
            for (int j = 0; j < flightsArray.size(); j++) {
                JsonObject flightJson = flightsArray.get(j).getAsJsonObject();
                Flight flight = new Flight();
                flight.setNumber(flightJson.get("flight_number").getAsString().substring(3));
                List<Airline> airlineSearch = (airlineRepository.findByName(flightJson.get("airline").getAsString()));
                Airline airline;
                if (airlineSearch.size() == 0) {
                    airline = new Airline();
                    airline.setName(flightJson.get("airline").getAsString());
                    airline.setIataCode(flightJson.get("flight_number").getAsString().substring(0, 2));
                    airlineRepository.save(airline);
                } else {
                    airline = airlineSearch.get(0);
                }

                JsonObject departureAirportJson = flightJson.getAsJsonObject("departure_airport");
                JsonObject arrivalAirportJson = flightJson.getAsJsonObject("arrival_airport");
                String departureIataCode = departureAirportJson.get("id").getAsString();
                String arrivalIataCode = arrivalAirportJson.get("id").getAsString();

                String departureAirportName = departureAirportJson.get("name").getAsString();
                String arrivalAirportName = arrivalAirportJson.get("name").getAsString();

                Airport departureAirport = findOrCreateAirport(departureIataCode, departureAirportName);
                flight.setOrigin(departureAirport);

                Airport arrivalAirport = findOrCreateAirport(arrivalIataCode, arrivalAirportName);
                flight.setDestination(arrivalAirport);

                flight.setAirline(airline);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime departureDateTime = LocalDateTime.parse(departureAirportJson.get("time").getAsString(), formatter);
                LocalDateTime arrivalDateTime = LocalDateTime.parse(arrivalAirportJson.get("time").getAsString(), formatter);
                flight.setDepartureTime(departureDateTime.atOffset(ZoneOffset.UTC));
                flight.setArrivalTime(arrivalDateTime.atOffset(ZoneOffset.UTC));

                flightSet.add(flight);
                flightRepository.save(flight);
            }
            ticket.setFlights(flightSet);
            TicketRequestDTO ticketRequestDTO = ticketMapper.toRequestDTO(ticket);
            TicketDTO ticketDTO = create(ticketRequestDTO);
            ticket.setId(ticketDTO.getId());
            tickets.add(ticket);
        }
        return tickets;
    }


    private Airport findOrCreateAirport(String iataCode, String airportName) {
        List<Airport> airportSearch = airportRepository.findByIataCode(iataCode);
        if (airportSearch.size() == 0) {
            Airport airport = new Airport();
            airport.setName(airportName);
            airport.setIataCode(iataCode);
            airportRepository.save(airport);
            return airport;
        } else {
            return airportSearch.get(0);
        }
    }



}
