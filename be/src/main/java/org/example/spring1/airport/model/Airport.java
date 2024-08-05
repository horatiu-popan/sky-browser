package org.example.spring1.airport.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.spring1.city.model.City;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 3, nullable = false)
    private String iataCode;

    @Column(length = 512)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id", nullable = true)
    private City city;
}
