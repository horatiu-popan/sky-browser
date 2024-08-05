package org.example.spring1.airport;

import org.example.spring1.airport.model.Airport;
import org.example.spring1.airport.model.AirportDTO;
import org.example.spring1.airport.model.AirportRequestDTO;
import org.example.spring1.city.model.City;
import org.example.spring1.city.model.CityDTO;
import org.example.spring1.core.SpringControllerBaseTest;
import org.example.spring1.global.SingleBodyRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.spring1.UrlMapping.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AirportControllerTest extends SpringControllerBaseTest {

    @InjectMocks
    private AirportController airportController;

    @Mock
    private AirportService airportService;

    @BeforeEach
    public void setUp() {
        super.setUp();
        airportController = new AirportController(airportService);
        mvc = buildForController(airportController);
    }

    @Test
    void findAll() throws Exception {
        List<Airport> airports = new ArrayList<>();
        int nrItems = 10;
        for (int i = 0; i < nrItems; i++) {
            airports.add(Airport.builder().name(String.valueOf(i)).build());
        }
        when(airportService.findAll()).thenReturn(airports);

        ResultActions result = performGet(AIRPORTS);

        result.andExpect(status().isOk()).andExpect(contentToBe(airports));
    }

    @Test
    void findAllFiltered() throws Exception{
        Long cityId = 1L;
        City city = new City(1L, "TestCity");
        List<Airport> expectedAirports = Arrays.asList(
                new Airport(1L, "AIR", "Airport1", city),
                new Airport(2L, "AI2","Airport2", city)
        );
        when(airportService.findAllFiltered(cityId)).thenReturn(expectedAirports);
        String url = AIRPORTS + FILTERED_PART + "?cityId=" + cityId;
        ResultActions result = performGet(url);
        result.andExpect(status().isOk()).andExpect(contentToBe(expectedAirports));

    }

    @Test
    void get() throws Exception {
        long id = 1L;
        AirportDTO dto = AirportDTO.builder().id(id).name("name").iataCode("AIR").cityDTO(new CityDTO()).build();
        ResponseEntity<?> res = ResponseEntity.ok(dto);

        doReturn(res).when(airportService).get(id);

        ResultActions resultActions = performGetWithPathVariables(AIRPORTS + ID_PART, id);

        verify(airportService, times(1)).get(id);

        resultActions.andExpect(status().isOk()).andExpect(contentToBe(dto));
    }



    @Test
    void getNotFound() throws Exception {
        long id = 1L;
        ResponseEntity<?> res = ResponseEntity.notFound().build();

        doReturn(res).when(airportService).get(id);

        ResultActions resultActions = performGetWithPathVariables(AIRPORTS + ID_PART, id);

        verify(airportService, times(1)).get(id);

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        AirportRequestDTO requestDto = AirportRequestDTO.builder()
                .iataCode("ABC")
                .name("Airport Name")
                .cityId(1L)
                .build();

        AirportDTO responseDto = AirportDTO.builder()
                .id(1L)
                .iataCode("ABC")
                .name("Airport Name")
                .cityDTO(new CityDTO())
                .build();

        when(airportService.create(requestDto)).thenReturn(responseDto);
        ResultActions resultActions = performPostWithRequestBody(AIRPORTS, requestDto);
        verify(airportService, times(1)).create(requestDto);
        resultActions.andExpect(status().isOk())
                .andExpect(contentToBe(responseDto));
    }


    @Test
    void delete() throws Exception {
        Long idToDelete = 1L;
        doNothing().when(airportService).delete(idToDelete);
        ResultActions resultActions = performDeleteWithPathVariables(AIRPORTS + ID_PART, idToDelete);
        verify(airportService, times(1)).delete(idToDelete);
        resultActions.andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        Long idToUpdate = 1L;
        AirportRequestDTO requestDto = AirportRequestDTO.builder()
                .iataCode("XYZ")
                .name("Updated Airport Name")
                .cityId(2L)
                .build();

        AirportDTO responseDto = AirportDTO.builder()
                .id(idToUpdate)
                .iataCode("XYZ")
                .name("Updated Airport Name")
                .cityDTO(new CityDTO())
                .build();

        when(airportService.update(idToUpdate, requestDto)).thenReturn(responseDto);

        ResultActions resultActions = performPutWithPathVariableAndRequestBody(AIRPORTS + ID_PART, idToUpdate, requestDto);

        verify(airportService, times(1)).update(idToUpdate, requestDto);

        resultActions.andExpect(status().isOk())
                .andExpect(contentToBe(responseDto));
    }


    @Test
    void changeIataCode() throws Exception {
        Long airportId = 1L;
        SingleBodyRequestDTO<String> requestDto = new SingleBodyRequestDTO<>("NEW");
        AirportDTO responseDto = AirportDTO.builder()
                .id(airportId)
                .iataCode("NEW")
                .name("Airport Name")
                .cityDTO(new CityDTO())
                .build();
        when(airportService.changeIataCode(airportId, requestDto.getBody())).thenReturn(responseDto);
        ResultActions resultActions = performPatchWithPathVariableAndRequestBody(AIRPORTS + ID_PART + CHANGE_IATA_CODE_PART, airportId, requestDto);
        verify(airportService, times(1)).changeIataCode(airportId, requestDto.getBody());
        resultActions.andExpect(status().isOk())
                .andExpect(contentToBe(responseDto));
    }

}