package org.example.spring1.city;

import org.example.spring1.airport.AirportController;
import org.example.spring1.airport.AirportService;
import org.example.spring1.airport.model.Airport;
import org.example.spring1.airport.model.AirportDTO;
import org.example.spring1.airport.model.AirportRequestDTO;
import org.example.spring1.city.model.City;
import org.example.spring1.city.model.CityDTO;
import org.example.spring1.city.model.CityRequestDTO;
import org.example.spring1.core.SpringControllerBaseTest;
import org.example.spring1.global.SingleBodyRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.example.spring1.UrlMapping.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CityControllerTest extends SpringControllerBaseTest {

    @InjectMocks
    private CityController cityController;

    @Mock
    private CityService cityService;

    @BeforeEach
    public void setUp() {
        super.setUp();
        cityController = new CityController(cityService);
        mvc = buildForController(cityController);
    }

    @Test
    void findAll() throws Exception {
        List<City> cities = new ArrayList<>();
        int nrItems = 10;
        for (int i = 0; i < nrItems; i++) {
            cities.add(City.builder().name(String.valueOf(i)).build());
        }
        when(cityService.findAll()).thenReturn(cities);

        ResultActions result = performGet(CITIES);

        result.andExpect(status().isOk()).andExpect(contentToBe(cities));
    }

    @Test
    void get() throws Exception {
        long id = 1L;
        CityDTO dto = CityDTO.builder().id(id).name("name").build();
        ResponseEntity<?> res = ResponseEntity.ok(dto);

        doReturn(res).when(cityService).get(id);

        ResultActions resultActions = performGetWithPathVariables(CITIES + ID_PART, id);

        verify(cityService, times(1)).get(id);

        resultActions.andExpect(status().isOk()).andExpect(contentToBe(dto));
    }

    @Test
    void getNotFound() throws Exception {
        long id = 1L;
        ResponseEntity<?> res = ResponseEntity.notFound().build();

        doReturn(res).when(cityService).get(id);

        ResultActions resultActions = performGetWithPathVariables(CITIES + ID_PART, id);

        verify(cityService, times(1)).get(id);

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        CityRequestDTO requestDto = CityRequestDTO.builder()
                .name("Name")
                .build();

        CityDTO responseDto = CityDTO.builder()
                .id(1L)
                .name("Name")
                .build();

        when(cityService.create(requestDto)).thenReturn(responseDto);
        ResultActions resultActions = performPostWithRequestBody(CITIES, requestDto);
        verify(cityService, times(1)).create(requestDto);
        resultActions.andExpect(status().isOk())
                .andExpect(contentToBe(responseDto));
    }

    @Test
    void delete() throws Exception{
        Long idToDelete = 1L;
        doNothing().when(cityService).delete(idToDelete);
        ResultActions resultActions = performDeleteWithPathVariables(CITIES + ID_PART, idToDelete);
        verify(cityService, times(1)).delete(idToDelete);
        resultActions.andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        Long idToUpdate = 1L;
        CityRequestDTO requestDto = CityRequestDTO.builder()
                .name("Updated Airport Name")
                .build();

        CityDTO responseDto = CityDTO.builder()
                .id(idToUpdate)
                .name("Updated Airport Name")
                .build();

        when(cityService.update(idToUpdate, requestDto)).thenReturn(responseDto);

        ResultActions resultActions = performPutWithPathVariableAndRequestBody(CITIES + ID_PART, idToUpdate, requestDto);

        verify(cityService, times(1)).update(idToUpdate, requestDto);

        resultActions.andExpect(status().isOk())
                .andExpect(contentToBe(responseDto));
    }
}