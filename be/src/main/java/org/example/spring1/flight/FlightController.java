package org.example.spring1.flight;

import lombok.RequiredArgsConstructor;
import org.example.spring1.flight.model.Flight;
import org.example.spring1.flight.model.FlightDTO;
import org.example.spring1.flight.model.FlightRequestDTO;
import org.example.spring1.flight.model.FlightSearchDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.spring1.UrlMapping.*;

@RestController
@RequestMapping(FLIGHTS)
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;
    @GetMapping
    public List<Flight> findAll() {
        return flightService.findAll();
    }

    @GetMapping(ID_PART)
    public ResponseEntity<?> get(@PathVariable Long id) {
        return flightService.get(id);
    }

    @PostMapping
    public FlightDTO create(@RequestBody FlightRequestDTO dto) {
        return flightService.create(dto);
    }

    @DeleteMapping(ID_PART)
    public void delete(@PathVariable Long id) {
        flightService.delete(id);
    }


    @PostMapping(FILTERED_PART)
    public List<FlightDTO> findAllFiltered(@RequestBody FlightSearchDTO searchDTO) {
        return flightService.findAllFiltered(searchDTO);
    }

    @PutMapping(ID_PART)
    public FlightDTO update(@PathVariable Long id, @RequestBody FlightRequestDTO dto) {
        return flightService.update(id, dto);
    }
}
