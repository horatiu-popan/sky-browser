package org.example.spring1.booking;

import org.example.spring1.booking.model.Booking;
import org.example.spring1.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
}
