import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import 'react-datepicker/dist/react-datepicker.css';
import { useNavigate } from 'react-router-dom'; 
import './BookingPage.css';

const BookingPage = () => {
  const { ticketId } = useParams();
  const [ticket, setTicket] = useState(null);
  const [passengers, setPassengers] = useState([{ name: '', passportNumber: '' }]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchTicketDetails = async () => {
      try {
        const response = await fetch(`/api/tickets/${ticketId}`);
        const data = await response.json();
        setTicket(data);
      } catch (error) {
        console.error('Error fetching ticket details:', error);
      }
    };

    fetchTicketDetails();
  }, [ticketId]);

  const handleChange = (e, index) => {
    const { name, value } = e.target;
    const updatedPassengers = [...passengers];
    updatedPassengers[index] = {
      ...updatedPassengers[index],
      [name]: value
    };
    setPassengers(updatedPassengers);
  };

  const addPassenger = () => {
    setPassengers([...passengers, { name: '', passportNumber: '' }]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      const response = await fetch('/api/bookings', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
          number: 'ABC123',
          ticketId: parseInt(ticketId),
          passengers: passengers
        })
      });
      if (response.ok) {
        const bookingDetails = await response.json();
        navigate(`/booking/${bookingDetails.id}/details`); 
      } else {
        console.error('Failed to submit passenger details');
      }
    } catch (error) {
      console.error('Error submitting passenger details:', error);
    }
  };

  return (
    <div className="booking-page">
      <h2>Booking Details</h2>
      {ticket && (
        <div className="flight-details">
          <h3>Flight Details</h3>
          <p>Price: ${ticket.price}</p>
          <p>Number of Stops: {ticket.flights.length - 1}</p>
          <p>Duration: {calculateDuration(ticket.flights)}</p>
          {ticket.flights.map((flight, index) => (
            <div key={flight.id} className="flight-card">
              <h4>Flight {index + 1}</h4>
              <p>Airline: {flight.airlineDTO.name}</p>
              <p>Flight Number: {`${flight.airlineDTO.iataCode}${flight.number}`}</p>
              <p>Departure Airport: {flight.originAirportDTO.name} ({flight.originAirportDTO.iataCode})</p>
              <p>Arrival Airport: {flight.destinationAirportDTO.name} ({flight.destinationAirportDTO.iataCode})</p>
              <p>Departure Time: {flight.departureTime}</p>
              <p>Arrival Time: {flight.arrivalTime}</p>
            </div>
          ))}
        </div>
      )}
      <h3>Passenger Details</h3>
      <form onSubmit={handleSubmit} className="passenger-form">
        {passengers.map((passenger, index) => (
          <div key={index} className="passenger-input">
            <label>
              Name:
              <input
                type="text"
                name="name"
                value={passenger.name}
                onChange={(e) => handleChange(e, index)}
              />
            </label>
            <br />
            <label>
              Passport Number:
              <input
                type="text"
                name="passportNumber"
                value={passenger.passportNumber}
                onChange={(e) => handleChange(e, index)}
              />
            </label>
            <br />
          </div>
        ))}
        <button type="button" onClick={addPassenger} className="add-passenger-button">Add Passenger</button>
        <br />
        <button type="submit" className="submit-button">Submit</button>
      </form>
    </div>
  );
};

const calculateDuration = (flights) => {
  const departureTime = new Date(flights[0].departureTime);
  const arrivalTime = new Date(flights[flights.length - 1].arrivalTime);
  const durationMinutes = Math.abs(arrivalTime - departureTime) / (1000 * 60);
  const hours = Math.floor(durationMinutes / 60);
  const minutes = durationMinutes % 60;
  return `${hours}h${minutes}m`;
};

export default BookingPage;
