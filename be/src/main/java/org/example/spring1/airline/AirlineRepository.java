package org.example.spring1.airline;

import org.example.spring1.airline.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AirlineRepository extends JpaRepository<Airline, Long> {
    List<Airline> findByName(String name);
}
