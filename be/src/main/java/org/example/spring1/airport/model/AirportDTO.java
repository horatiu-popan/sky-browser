package org.example.spring1.airport.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.spring1.city.model.CityDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirportDTO {
    private Long id;
    private String iataCode;
    private String name;
    private CityDTO cityDTO;
}
