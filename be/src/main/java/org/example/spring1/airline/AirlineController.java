package org.example.spring1.airline;

import lombok.RequiredArgsConstructor;
import org.example.spring1.airline.model.Airline;
import org.example.spring1.airline.model.AirlineDTO;
import org.example.spring1.airline.model.AirlineRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.spring1.UrlMapping.*;

@RestController
@RequestMapping(AIRLINES)
@RequiredArgsConstructor
public class AirlineController {
    private final AirlineService airlineService;

    @GetMapping
    public List<Airline> findAll() {
        return airlineService.findAll();
    }

    @GetMapping(ID_PART)
    public ResponseEntity<?> get(@PathVariable Long id) {
        return airlineService.get(id);
    }

    @PostMapping
    public AirlineDTO create(@RequestBody AirlineRequestDTO dto) {
        return airlineService.create(dto);
    }

    @DeleteMapping(ID_PART)
    public void delete(@PathVariable Long id) {
        airlineService.delete(id);
    }

    @PutMapping(ID_PART)
    public AirlineDTO update(@PathVariable Long id, @RequestBody AirlineRequestDTO dto) {
        return airlineService.update(id, dto);
    }
}
