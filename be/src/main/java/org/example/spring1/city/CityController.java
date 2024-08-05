package org.example.spring1.city;

import lombok.RequiredArgsConstructor;
import org.example.spring1.city.model.City;
import org.example.spring1.city.model.CityDTO;
import org.example.spring1.city.model.CityRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.spring1.UrlMapping.*;

@RestController
@RequestMapping(CITIES)
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;

    @GetMapping
    public List<City> findAll() {
        return cityService.findAll();
    }

    @GetMapping(ID_PART)
    public ResponseEntity<?> get(@PathVariable Long id) {
        return cityService.get(id);
    }

    @PostMapping
    public CityDTO create(@RequestBody CityRequestDTO dto) {
        return cityService.create(dto);
    }

    @DeleteMapping(ID_PART)
    public void delete(@PathVariable Long id) {
        cityService.delete(id);
    }

    @PutMapping(ID_PART)
    public CityDTO update(@PathVariable Long id, @RequestBody CityRequestDTO dto) {
        return cityService.update(id, dto);
    }
}
