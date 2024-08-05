package org.example.spring1.airport;

import org.example.spring1.airport.model.Airport;
import org.example.spring1.city.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    List<Airport> findByIataCode(String iataCode);
    List<Airport> findByCity(City city);
}

