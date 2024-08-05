package org.example.spring1.flight.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.spring1.airline.model.Airline;
import org.example.spring1.airport.model.Airport;
import org.example.spring1.ticket.model.Ticket;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    @Column(length = 4, nullable = false)
    private String number;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "origin_id", nullable = false)
    private Airport origin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_id", nullable = false)
    private Airport destination;

    @Column(nullable = false)
    private OffsetDateTime departureTime;

    @Column(nullable = false)
    private OffsetDateTime  arrivalTime;

    @ManyToMany(mappedBy = "flights")
    private Set<Ticket> tickets = new HashSet<>();
}
