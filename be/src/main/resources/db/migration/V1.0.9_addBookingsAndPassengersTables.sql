-- Create Booking Table
CREATE TABLE Booking (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         number VARCHAR(512) NOT NULL,
                         ticket_id BIGINT NOT NULL,
                         user_id BIGINT NOT NULL,
                         CONSTRAINT fk_ticket FOREIGN KEY (ticket_id) REFERENCES Ticket(id),
                         CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES User(id)
);

-- Create Passenger Table
CREATE TABLE Passenger (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           passport_number VARCHAR(255) NOT NULL,
                           booking_id BIGINT NOT NULL,
                           CONSTRAINT fk_booking FOREIGN KEY (booking_id) REFERENCES Booking(id) ON DELETE CASCADE
);
