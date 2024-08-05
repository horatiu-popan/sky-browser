package org.example.spring1.city;

import lombok.RequiredArgsConstructor;
import org.example.spring1.city.model.City;
import org.example.spring1.city.model.CityDTO;
import org.example.spring1.city.model.CityRequestDTO;
import org.example.spring1.exception.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public ResponseEntity<?> get(Long id) {
        return cityRepository.findById(id)
                .map(cityMapper::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public CityDTO create(CityRequestDTO dto) {
        City city = new City();
        city.setName(dto.getName());
        City savedCity = cityRepository.save(city);

        return cityMapper.fromEntity(savedCity);
    }

    public void delete(Long id) {
        cityRepository.deleteById(id);
    }


    public CityDTO update(Long id, CityRequestDTO dto) {
        City existingCity = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("City not found with id: " + id));
        existingCity.setName(dto.getName());
        City updatedCity = cityRepository.save(existingCity);
        return cityMapper.fromEntity(updatedCity);
    }
}
