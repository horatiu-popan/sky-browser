import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import TicketSearch from './TicketSearch';
import Header from './Header';
import './HomePage.css'; // Make sure to import the CSS file

const HomePage = () => {
  const [bookings, setBookings] = useState([]);
  const [loggedInUser, setLoggedInUser] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchBookings = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await fetch('/api/bookings/user', {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${token}`
            }});
        const data = await response.json();
        setBookings(data);
      } catch (error) {
        console.error('Error fetching bookings:', error);
      }
    };

    fetchBookings();
  }, []);

  const handleBookingClick = (id) => {
    navigate(`/booking/${id}/details`);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    setLoggedInUser('');
    window.location.href = '/';
  };

  return (
    <div className="home-page">
      <Header onLogout={handleLogout} />
      <h1>Home Page</h1>
      <h2>Your Bookings</h2>
      <ul className="booking-list">
        {bookings.map((booking) => (
          <li key={booking.id} onClick={() => handleBookingClick(booking.id)} className="booking-item">
            <p>Booking reference: <span className="booking-number">{booking.number}</span></p>
          </li>
        ))}
      </ul>
      <TicketSearch />
    </div>
  );
};

export default HomePage;
