import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import './BookingDetailsPage.css';

const BookingDetailsPage = () => {
  const { bookingId } = useParams();
  const [bookingDetails, setBookingDetails] = useState(null);
  const [flights, setFlights] = useState([]);

  useEffect(() => {
    const fetchBookingDetails = async () => {
      try {
        const response = await fetch(`/api/bookings/${bookingId}`);
        const data = await response.json();
        setBookingDetails(data);

        const ticketId = data.ticketDTO.id;
        console.log(data);
        const ticketResponse = await fetch(`/api/tickets/${ticketId}`);
        const ticketData = await ticketResponse.json();
        setFlights(ticketData.flights);
        console.log(ticketData.flights);
      } catch (error) {
        console.error('Error fetching booking details:', error);
      }
    };

    fetchBookingDetails();
  }, [bookingId]);

  if (!bookingDetails) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="booking-details">
      <h2>Booking Details</h2>
      <p>Booking Reference: {bookingDetails.number}</p>
      <h3>Flights</h3>
      <ul>
        {flights.sort((a, b) => new Date(a.departureTime) - new Date(b.departureTime)).map((flight, index) => (
          <div key={flight.id} className="flight-card">
            <h4>Flight {index + 1}</h4>
            <table className="flight-table">
              <tbody>
                <tr>
                  <td>Airline:</td>
                  <td>{flight.airlineDTO.name}</td>
                </tr>
                <tr>
                  <td>Flight Number:</td>
                  <td>{`${flight.airlineDTO.iataCode}${flight.number}`}</td>
                </tr>
                <tr>
                  <td>Departure Airport:</td>
                  <td>{flight.originAirportDTO.name} ({flight.originAirportDTO.iataCode})</td>
                </tr>
                <tr>
                  <td>Arrival Airport:</td>
                  <td>{flight.destinationAirportDTO.name} ({flight.destinationAirportDTO.iataCode})</td>
                </tr>
                <tr>
                  <td>Departure Time:</td>
                  <td>{flight.departureTime}</td>
                </tr>
                <tr>
                  <td>Arrival Time:</td>
                  <td>{flight.arrivalTime}</td>
                </tr>
              </tbody>
            </table>
            {index < flights.length - 1 && (
              <p className="waiting-time">Waiting Time for Next Flight: {calculateWaitingTime(flight, flights[index + 1])}</p>
            )}
          </div>
        ))}
      </ul>
      <h3>Passengers</h3>
      <ul className="passenger-list">
        {bookingDetails.passengers.map((passenger, index) => (
          <li key={index} className="passenger-item">
            <p>Name: {passenger.name}</p>
            <p>Passport Number: {passenger.passportNumber}</p>
          </li>
        ))}
      </ul>
    </div>
  );
};

const calculateWaitingTime = (currentFlight, nextFlight) => {
  const currentArrivalTime = new Date(currentFlight.arrivalTime);
  const nextDepartureTime = new Date(nextFlight.departureTime);
  const waitingTimeMinutes = Math.abs(nextDepartureTime - currentArrivalTime) / (1000 * 60);
  const hours = Math.floor(waitingTimeMinutes / 60);
  const minutes = waitingTimeMinutes % 60;
  return `${hours}h${minutes}m`;
};

export default BookingDetailsPage;
