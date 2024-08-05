import TicketSearch from './TicketSearch'
import LoginPage from './LoginPage';
import BookingPage from './BookingPage';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import BookingDetailsPage from './BookingDetailsPage';
import HomePage from './HomePage';
import RegisterPage from './RegisterPage';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/search-flights" element={<TicketSearch/>} />
          <Route path="/booking/:ticketId" element={<BookingPage />} />
          <Route path="/booking/:bookingId/details" element={<BookingDetailsPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
