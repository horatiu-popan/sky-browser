package org.example.spring1.airline;

import lombok.RequiredArgsConstructor;
import org.example.spring1.airline.model.Airline;
import org.example.spring1.airline.model.AirlineDTO;
import org.example.spring1.airline.model.AirlineRequestDTO;
import org.example.spring1.exception.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirlineService {
    private final AirlineRepository airlineRepository;
    private final AirlineMapper airlineMapper;
    public List<Airline> findAll() {
        return airlineRepository.findAll();
    }

    public ResponseEntity<?> get(Long id) {
        return airlineRepository.findById(id)
                .map(airlineMapper::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public AirlineDTO create(AirlineRequestDTO dto) {
        Airline airline = new Airline();
        airline.setName(dto.getName());
        airline.setIataCode(dto.getIataCode());
        Airline savedAirline = airlineRepository.save(airline);
        return airlineMapper.fromEntity(savedAirline);
    }

    public void delete(Long id) {
        airlineRepository.deleteById(id);
    }

    public AirlineDTO update(Long id, AirlineRequestDTO dto) {
        Airline existingAirline = airlineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Airline not found with id: " + id));
        existingAirline.setName(dto.getName());
        existingAirline.setIataCode(dto.getIataCode());
        Airline updatedAirline = airlineRepository.save(existingAirline);
        return airlineMapper.fromEntity(updatedAirline);
    }
}
