CREATE TABLE Ticket (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        origin_id BIGINT NOT NULL,
                        destination_id BIGINT NOT NULL,
                        price DOUBLE NOT NULL,
                        CONSTRAINT fk_ticket_origin FOREIGN KEY (origin_id) REFERENCES Airport(id),
                        CONSTRAINT fk_ticket_destination FOREIGN KEY (destination_id) REFERENCES Airport(id)
);

CREATE TABLE ticket_flights (
                                ticket_id BIGINT NOT NULL,
                                flight_id BIGINT NOT NULL,
                                PRIMARY KEY (ticket_id, flight_id),
                                CONSTRAINT fk_ticket_flight_ticket FOREIGN KEY (ticket_id) REFERENCES Ticket(id),
                                CONSTRAINT fk_ticket_flight_flight FOREIGN KEY (flight_id) REFERENCES Flight(id)
);
