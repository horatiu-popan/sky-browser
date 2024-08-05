package org.example.spring1.airport;

import org.example.spring1.airport.model.Airport;
import org.example.spring1.airport.model.AirportDTO;
import org.example.spring1.airport.model.AirportRequestDTO;
import org.example.spring1.city.CityRepository;
import org.example.spring1.city.model.City;
import org.example.spring1.city.model.CityDTO;
import org.example.spring1.item.ItemMapper;
import org.example.spring1.item.ItemRepository;
import org.example.spring1.item.ItemService;
import org.example.spring1.item.model.Item;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AirportServiceTest {

    private AirportService airportService;

    @Mock
    private AirportRepository airportRepository;
    @Mock
    private AirportMapper airportMapper;
    @Mock
    private CityRepository cityRepository;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        airportService = new AirportService(airportRepository, airportMapper, cityRepository);
    }

    @Test
    void findAll() {
        Airport airport1 = Airport.builder().id(-1L).build();
        Airport airport2 = Airport.builder().id(0L).name("airport2").build();
        Airport airport3 = Airport.builder().id(1L).name("airport3").build();

        List<Airport> preparedAirports = List.of(airport1, airport2, airport3);

        when(airportRepository.findAll()).thenReturn(preparedAirports);

        List<Airport> returnedItems = airportService.findAll();
        assertEquals(preparedAirports.size(), returnedItems.size());
        assertEquals(preparedAirports, returnedItems);
    }
    @Test
    void findAllFiltered() {
        City city = new City();
        city.setId(1L);
        Airport airport1 = Airport.builder().id(1L).city(city).build();
        Airport airport2 = Airport.builder().id(2L).city(city).build();
        List<Airport> airports = new ArrayList<>();
        airports.add(airport1);
        airports.add(airport2);
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(airportRepository.findByCity(city)).thenReturn(airports);
        List<Airport> result = airportService.findAllFiltered(1L);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(airport1));
        assertTrue(result.contains(airport2));
    }
    @Test
    void get() {
        Long id = 1L;
        Airport airport = new Airport();
        when(airportRepository.findById(id)).thenReturn(Optional.of(airport));
        AirportDTO airportDTO = new AirportDTO();
        when(airportMapper.fromEntity(airport)).thenReturn(airportDTO);
        ResponseEntity<?> response = airportService.get(id);
        assertNotNull(response);
        assertEquals(ResponseEntity.ok(airportDTO), response);
    }

    @Test
    public void testGetAirportByIdNotFound() {
        Long id = 2L;
        when(airportRepository.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<?> response = airportService.get(id);
        assertNotNull(response);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    void update() {
        Long id = 1L;
        Airport existingAirportEntity = new Airport();
        when(airportRepository.findById(id)).thenReturn(Optional.of(existingAirportEntity));

        AirportRequestDTO dto = new AirportRequestDTO();
        dto.setName("Updated Airport Name");
        dto.setIataCode("UAN");
        Long cityId = 1L;
        dto.setCityId(cityId);

        City cityEntity = City.builder().id(cityId).build();
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(cityEntity));

        Airport updatedAirportEntity = new Airport();
        updatedAirportEntity.setId(id);
        updatedAirportEntity.setName(dto.getName());
        updatedAirportEntity.setIataCode(dto.getIataCode());
        updatedAirportEntity.setCity(cityEntity);
        when(airportRepository.save(existingAirportEntity)).thenReturn(updatedAirportEntity);

        AirportDTO updatedAirportDTO = new AirportDTO();
        updatedAirportDTO.setName(updatedAirportEntity.getName());
        updatedAirportDTO.setIataCode(updatedAirportEntity.getIataCode());
        updatedAirportDTO.setCityDTO(CityDTO.builder().id(cityId).build());
        when(airportMapper.fromEntity(updatedAirportEntity)).thenReturn(updatedAirportDTO);

        AirportDTO result = airportService.update(id, dto);

        assertNotNull(result);
        assertEquals(updatedAirportDTO, result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getIataCode(), result.getIataCode());
        assertEquals(cityId, result.getCityDTO().getId());
    }
    @Test
    void create() {
        AirportRequestDTO dto = new AirportRequestDTO();
        dto.setName("New Airport Name");
        dto.setIataCode("AAA");
        Long cityId = 1L;
        dto.setCityId(cityId);

        City cityEntity = City.builder().id(cityId).build();
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(cityEntity));

        Airport savedAirportEntity = Airport.builder().id(1L)
                .name(dto.getName())
                .iataCode(dto.getIataCode())
                .city(cityEntity)
                .build();
        when(airportRepository.save(any())).thenReturn(savedAirportEntity);

        AirportDTO savedAirportDTO = AirportDTO.builder().id(1L)
                .name(dto.getName())
                .iataCode(dto.getIataCode())
                .cityDTO(CityDTO.builder().id(cityId).build())
                .build();
        when(airportMapper.fromEntity(savedAirportEntity)).thenReturn(savedAirportDTO);

        AirportDTO result = airportService.create(dto);

        assertNotNull(result);
        assertEquals(savedAirportDTO, result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getIataCode(), result.getIataCode());
        assertEquals(cityEntity.getId(), result.getCityDTO().getId());
    }

    @Test
    void delete() {
        Long id = 1L;
        assertDoesNotThrow(() -> airportService.delete(id));
        verify(airportRepository, times(1)).deleteById(id);
    }

    @Test
    void changeIataCode() {
        Long id = 1L;
        String newIataCode = "NEW";

        Airport existingAirportEntity = Airport.builder().id(id).build();
        when(airportRepository.findById(id)).thenReturn(Optional.of(existingAirportEntity));

        Airport updatedAirportEntity = Airport.builder().id(id).iataCode(newIataCode).build();
        when(airportRepository.save(existingAirportEntity)).thenReturn(updatedAirportEntity);

        AirportDTO updatedAirportDTO = AirportDTO.builder().iataCode(newIataCode).build();
        when(airportMapper.fromEntity(updatedAirportEntity)).thenReturn(updatedAirportDTO);
        AirportDTO result = airportService.changeIataCode(id, newIataCode);
        assertNotNull(result);
        assertEquals(updatedAirportDTO, result);
        assertEquals(newIataCode, result.getIataCode());
    }


}