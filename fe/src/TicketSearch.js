import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { useNavigate } from 'react-router-dom'; 
import './TicketSearch.css'; // Import the CSS file

const TicketSearch = () => {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useState({
    originAirport: '',
    destinationAirport: '',
    departureDate: new Date()
  });
  const [searchResults, setSearchResults] = useState([]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setSearchParams(prevParams => ({
      ...prevParams,
      [name]: value
    }));
  };

  const handleDateChange = (date) => {
    setSearchParams(prevParams => ({
      ...prevParams,
      departureDate: date
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('/api/tickets/filtered', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(searchParams)
      });
      const data = await response.json();
      setSearchResults(data);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  const handleBookingClick = (ticketId) => {
    // Redirect to booking page with ticketId
    navigate(`/booking/${ticketId}`); // Use navigate hook to redirect
  };

  return (
    <div className="ticket-search">
      <h2>Flight Search</h2>
      <form onSubmit={handleSubmit} className="search-form">
        <label>
          Origin Airport:
          <input
            type="text"
            name="originAirport"
            value={searchParams.originAirport}
            onChange={handleChange}
            className="search-input"
          />
        </label>
        <br />
        <label>
          Destination Airport:
          <input
            type="text"
            name="destinationAirport"
            value={searchParams.destinationAirport}
            onChange={handleChange}
            className="search-input"
          />
        </label>
        <br />
        <label>
          Departure Date:
          <DatePicker
            selected={searchParams.departureDate}
            onChange={handleDateChange}
            dateFormat="yyyy-MM-dd"
            className="date-picker"
          />
        </label>
        <br />
        <button type="submit" className="search-button">Search</button>
      </form>
      {searchResults.length > 0 && (
        <div className="search-results">
          <h3>Search Results</h3>
          {searchResults.map(ticket => (
            <div key={ticket.id} className="ticket-card">
              <p>Price: ${ticket.price}</p>
              <p>Number of Stops: {ticket.flights.length - 1}</p>
              <p>Duration: {calculateDuration(ticket.flights)}</p>
              {ticket.flights.sort((a, b) => new Date(a.departureTime) - new Date(b.departureTime)).map((flight, index) => (
                <div key={flight.id} className="flight-details">
                  <h4>Flight {index + 1}</h4>
                  <table>
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
                  {index < ticket.flights.length - 1 && (
                    <p>Waiting Time for Next Flight: {calculateWaitingTime(flight, ticket.flights[index + 1])}</p>
                  )}
                </div>
              ))}
              <button onClick={() => handleBookingClick(ticket.id)} className="book-button">Book</button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

// Function to calculate the total duration of the flight itinerary
const calculateDuration = (flights) => {
  const departureTime = new Date(flights[0].departureTime);
  const arrivalTime = new Date(flights[flights.length - 1].arrivalTime);
  const durationMinutes = Math.abs(arrivalTime - departureTime) / (1000 * 60); // Duration in minutes
  const hours = Math.floor(durationMinutes / 60);
  const minutes = durationMinutes % 60;
  return `${hours}h${minutes}m`;
};

const calculateWaitingTime = (currentFlight, nextFlight) => {
  const currentArrivalTime = new Date(currentFlight.arrivalTime);
  const nextDepartureTime = new Date(nextFlight.departureTime);
  const waitingTimeMinutes = Math.abs(nextDepartureTime - currentArrivalTime) / (1000 * 60); // Waiting time in minutes
  const hours = Math.floor(waitingTimeMinutes / 60);
  const minutes = waitingTimeMinutes % 60;
  return `${hours}h${minutes}m`;
};

export default TicketSearch;
