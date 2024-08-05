package org.example.spring1.city;

import org.example.spring1.airport.AirportMapper;
import org.example.spring1.airport.AirportRepository;
import org.example.spring1.airport.AirportService;
import org.example.spring1.airport.model.Airport;
import org.example.spring1.airport.model.AirportDTO;
import org.example.spring1.airport.model.AirportRequestDTO;
import org.example.spring1.city.model.City;
import org.example.spring1.city.model.CityDTO;
import org.example.spring1.city.model.CityRequestDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CityServiceTest {

    private CityService cityService;

    @Mock
    private CityRepository cityRepository;
    @Mock
    private CityMapper cityMapper;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cityService = new CityService(cityRepository, cityMapper);
    }

    @Test
    void findAll() {
        City item1 = City.builder().id(-1L).build();
        City item2 = City.builder().id(0L).name("item2").build();
        City item3 = City.builder().id(1L).name("item3").build();

        List<City> preparedItems = List.of(item1, item2, item3);

        when(cityRepository.findAll()).thenReturn(preparedItems);

        List<City> returnedItems = cityService.findAll();
        assertEquals(preparedItems.size(), returnedItems.size());
        assertEquals(preparedItems, returnedItems);
    }
    @Test
    void get() {
        Long id = 1L;
        City city = new City();
        when(cityRepository.findById(id)).thenReturn(Optional.of(city));
        CityDTO cityDTO = new CityDTO();
        when(cityMapper.fromEntity(city)).thenReturn(cityDTO);
        ResponseEntity<?> response = cityService.get(id);
        assertNotNull(response);
        assertEquals(ResponseEntity.ok(cityDTO), response);
    }
    @Test
    public void testGetCityByIdNotFound() {
        Long id = 2L;
        when(cityRepository.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<?> response = cityService.get(id);
        assertNotNull(response);
        assertEquals(ResponseEntity.notFound().build(), response);
    }
    @Test
    void update() {
        Long id = 1L;
        City existingCityEntity = new City();
        when(cityRepository.findById(id)).thenReturn(Optional.of(existingCityEntity));

        CityRequestDTO dto = CityRequestDTO.builder()
                .name("Updated Airport Name")
                .build();

        City updatedCityEntity = City.builder()
                .id(id)
                .name(dto.getName())
                .build();
        when(cityRepository.save(existingCityEntity)).thenReturn(updatedCityEntity);

        CityDTO updatedCityDTO = CityDTO.builder()
                .name(updatedCityEntity.getName())
                .build();
        when(cityMapper.fromEntity(updatedCityEntity)).thenReturn(updatedCityDTO);

        CityDTO result = cityService.update(id, dto);

        assertNotNull(result);
        assertEquals(updatedCityDTO, result);
        assertEquals(dto.getName(), result.getName());
    }
    @Test
    void create() {
        CityRequestDTO dto = CityRequestDTO.builder()
                .name("New Airport Name")
                .build();

        City savedCityEntity = City.builder()
                .id(1L)
                .name(dto.getName())
                .build();
        when(cityRepository.save(any())).thenReturn(savedCityEntity);

        CityDTO savedCityDTO = CityDTO.builder()
                .id(1L)
                .name(dto.getName())
                .build();
        when(cityMapper.fromEntity(savedCityEntity)).thenReturn(savedCityDTO);

        CityDTO result = cityService.create(dto);

        assertNotNull(result);
        assertEquals(savedCityDTO, result);
        assertEquals(dto.getName(), result.getName());
    }
    @Test
    void delete() {
        Long id = 1L;
        assertDoesNotThrow(() -> cityService.delete(id));
        verify(cityRepository, times(1)).deleteById(id);
    }
}
