package org.example.spring1.airport;

import lombok.RequiredArgsConstructor;
import org.example.spring1.airport.model.Airport;
import org.example.spring1.airport.model.AirportDTO;
import org.example.spring1.airport.model.AirportRequestDTO;
import org.example.spring1.global.SingleBodyRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.spring1.UrlMapping.*;

@RestController
@RequestMapping(AIRPORTS)
@RequiredArgsConstructor
public class AirportController {
    private final AirportService airportService;

    @GetMapping
    public List<Airport> findAll() {
        return airportService.findAll();
    }

    @GetMapping(FILTERED_PART)
    public List<Airport> findAllFiltered(@RequestParam Long cityId) {
        return airportService.findAllFiltered(cityId);
    }

    @GetMapping(ID_PART)
    public ResponseEntity<?> get(@PathVariable Long id) {
        return airportService.get(id);
    }

    @PostMapping
    public AirportDTO create(@RequestBody AirportRequestDTO dto) {
        return airportService.create(dto);
    }

    @DeleteMapping(ID_PART)
    public void delete(@PathVariable Long id) {
        airportService.delete(id);
    }

    @PutMapping(ID_PART)
    public AirportDTO update(@PathVariable Long id, @RequestBody AirportRequestDTO dto) {
        return airportService.update(id, dto);
    }

    @PatchMapping(ID_PART + CHANGE_IATA_CODE_PART)
    public AirportDTO changeIataCode(@PathVariable Long id, @RequestBody SingleBodyRequestDTO<String> dto) {
        return airportService.changeIataCode(id, dto.getBody());
    }
}
