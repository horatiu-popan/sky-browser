package org.example.spring1.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.spring1.passenger.model.PassengerDTO;
import org.example.spring1.ticket.model.TicketDTO;
import org.example.spring1.user.model.UserDTO;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private String number;
    private TicketDTO ticketDTO;
    private UserDTO userDTO;
    private Set<PassengerDTO> passengers;
}
