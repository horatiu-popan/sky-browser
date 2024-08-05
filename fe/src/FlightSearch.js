import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

const FlightSearch = () => {
  const [originAirport, setOriginAirport] = useState('');
  const [destinationAirport, setDestinationAirport] = useState('');
  const [departureDate, setDepartureDate] = useState(new Date());
  const [flights, setFlights] = useState([]);
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formattedDate = departureDate.toISOString().split('T')[0];

    const flightSearchData = {
      originAirport,
      destinationAirport,
      departureDate: formattedDate,
    };

    const apiEndpoint = '/api/flights/filtered';

    try {
      const response = await fetch(apiEndpoint, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(flightSearchData),
      });
      const data = await response.json();
      console.log('API Response:', data);

      setFlights(data);
      setSubmitted(true);

    } catch (error) {
      console.error('Error fetching flight data:', error);
    }
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="departureAirport">Departure Airport:</label>
          <input
            type="text"
            id="departureAirport"
            value={originAirport}
            onChange={(e) => setOriginAirport(e.target.value)}
          />
        </div>
        <div>
          <label htmlFor="arrivalAirport">Arrival Airport:</label>
          <input
            type="text"
            id="arrivalAirport"
            value={destinationAirport}
            onChange={(e) => setDestinationAirport(e.target.value)}
          />
        </div>
        <div>
          <label htmlFor="departureDate">Departure Date:</label>
          <DatePicker
            selected={departureDate}
            onChange={(date) => setDepartureDate(date)}
            dateFormat="yyyy-MM-dd"
          />
        </div>
        <button type="submit">Search Flights</button>
      </form>

      {/* Conditionally render flight results table or message */}
      {submitted && (
        <div>
          {flights.length > 0 ? (
            <div>
              <h2>Flight Search Results</h2>
              <table style={{ borderCollapse: 'collapse', border: '1px solid black' }}>
                <thead>
                  <tr>
                    <th style={{ border: '1px solid black', padding: '8px' }}>Airline</th>
                    <th style={{ border: '1px solid black', padding: '8px' }}>Flight Number</th>
                    <th style={{ border: '1px solid black', padding: '8px' }}>Origin</th>
                    <th style={{ border: '1px solid black', padding: '8px' }}>Destination</th>
                    <th style={{ border: '1px solid black', padding: '8px' }}>Departure Time</th>
                    <th style={{ border: '1px solid black', padding: '8px' }}>Arrival Time</th>
                  </tr>
                </thead>
                <tbody>
                  {flights.map((flight) => (
                    <tr key={flight.id}>
                      <td style={{ border: '1px solid black', padding: '8px' }}>{flight.airlineDTO.name}</td>
                      <td style={{ border: '1px solid black', padding: '8px' }}>{`${flight.airlineDTO.iataCode}${flight.number}`}</td>
                      <td style={{ border: '1px solid black', padding: '8px' }}>
                        [{flight.originAirportDTO.iataCode}] {flight.originAirportDTO.name}
                      </td>
                      <td style={{ border: '1px solid black', padding: '8px' }}>
                        [{flight.destinationAirportDTO.iataCode}] {flight.destinationAirportDTO.name}
                      </td>
                      <td style={{ border: '1px solid black', padding: '8px' }}>{flight.departureTime}</td>
                      <td style={{ border: '1px solid black', padding: '8px' }}>{flight.arrivalTime}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : (
            <p>No flights available. Please try another date.</p>
          )}
        </div>
      )}
    </div>
  );
};

export default FlightSearch;
