package org.example.spring1.airport;

import lombok.RequiredArgsConstructor;
import org.example.spring1.airport.model.Airport;
import org.example.spring1.airport.model.AirportDTO;
import org.example.spring1.airport.model.AirportRequestDTO;
import org.example.spring1.city.model.City;
import org.example.spring1.city.CityRepository;
import org.example.spring1.exception.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportService {

    private final AirportRepository airportRepository;
    private final AirportMapper airportMapper;
    private final CityRepository cityRepository;

    public List<Airport> findAll() {
        return airportRepository.findAll();
    }

    public ResponseEntity<?> get(Long id) {
        return airportRepository.findById(id)
                .map(airportMapper::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public AirportDTO create(AirportRequestDTO dto) {
        Airport airport = new Airport();
        airport.setName(dto.getName());
        airport.setIataCode(dto.getIataCode());
        City city = cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new EntityNotFoundException("City not found with id: " + dto.getCityId()));
        airport.setCity(city);
        Airport savedAirport = airportRepository.save(airport);

        return airportMapper.fromEntity(savedAirport);
    }

    public void delete(Long id) {
        airportRepository.deleteById(id);
    }

    public List<Airport> findAllFiltered(Long cityId) {
        return airportRepository.findByCity(cityRepository.findById(cityId).orElse(null));
    }

    public AirportDTO update(Long id, AirportRequestDTO dto) {
        Airport existingAirport = airportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Airport not found with id: " + id));
        existingAirport.setName(dto.getName());
        existingAirport.setIataCode(dto.getIataCode());
        City city = cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new EntityNotFoundException("City not found with id: " + dto.getCityId()));
        existingAirport.setCity(city);
        Airport updatedAirport = airportRepository.save(existingAirport);
        return airportMapper.fromEntity(updatedAirport);
    }

    public AirportDTO changeIataCode(Long id, String newIataCode) {
        Airport existingAirport = airportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Airport not found with id: " + id));

        existingAirport.setIataCode(newIataCode);
        Airport updatedAirport = airportRepository.save(existingAirport);
        return airportMapper.fromEntity(updatedAirport);
    }
}
