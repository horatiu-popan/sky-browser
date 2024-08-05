package org.example.spring1.booking;

import lombok.RequiredArgsConstructor;
import org.example.spring1.booking.model.Booking;
import org.example.spring1.booking.model.BookingDTO;
import org.example.spring1.booking.model.BookingRequestDTO;
import org.example.spring1.passenger.PassengerRepository;
import org.example.spring1.passenger.model.Passenger;
import org.example.spring1.ticket.TicketRepository;
import org.example.spring1.ticket.model.Ticket;
import org.example.spring1.user.UserRepository;
import org.example.spring1.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private static String generateBookingReference() { //random generator for booking reference
        byte[] randomBytes = new byte[7];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);

        String reference = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        reference = reference.substring(0, 7);

        return reference;
    }
    public BookingDTO createBooking(BookingRequestDTO bookingRequest, Principal principal) {
        Ticket ticket = ticketRepository.findById(bookingRequest.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = Booking.builder()
                .number(generateBookingReference())
                .ticket(ticket)
                .user(user)
                .build();

        List<Passenger> passengers = bookingRequest.getPassengers().stream()
                .map(req -> new Passenger(null, req.getName(), req.getPassportNumber(), booking))
                .collect(Collectors.toList());
        booking.setPassengers(new HashSet<>(passengers));
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.fromEntity(savedBooking);
    }

    public ResponseEntity<?> get(Long id) {
        return bookingRepository.findById(id)
                .map(bookingMapper::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    public List<BookingDTO> getBookingsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Booking> bookings = bookingRepository.findByUser(user);
        return bookings.stream()
                .map(bookingMapper::fromEntity)
                .collect(Collectors.toList());
    }
}
