package org.example.spring1.booking;

import lombok.RequiredArgsConstructor;
import org.example.spring1.booking.model.BookingDTO;
import org.example.spring1.booking.model.BookingRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static org.example.spring1.UrlMapping.BOOKINGS;
import static org.example.spring1.UrlMapping.ID_PART;


@RestController
@RequestMapping(BOOKINGS)
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping(ID_PART)
    public ResponseEntity<?> get(@PathVariable Long id) {
        return bookingService.get(id);
    }

    @PostMapping
    public BookingDTO createBooking(@RequestBody BookingRequestDTO bookingRequest, Principal principal) {
        return bookingService.createBooking(bookingRequest, principal);
    }

    @GetMapping("/user")
    public ResponseEntity<List<BookingDTO>> getBookingsForUser(Principal principal) {
        String username = principal.getName();
        List<BookingDTO> bookings = bookingService.getBookingsForUser(username);
        return ResponseEntity.ok(bookings);
    }
}
