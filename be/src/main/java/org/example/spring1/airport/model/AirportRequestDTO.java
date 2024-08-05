package org.example.spring1.airport.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirportRequestDTO {
    private String iataCode;
    private String name;
    private Long cityId;
}