const express = require('express');
const fetch = require('node-fetch');
const cors = require('cors');

const app = express();
const port = 5000;

// Middleware
app.use(express.json());
app.use(cors());

// Define route for flight search
app.get('/search', async (req, res) => {
  try {
    const { engine, departure_id, arrival_id, outbound_date, currency, hl } = req.query;
    const apiUrl = `https://serpapi.com/search.json?engine=${engine}&departure_id=${departure_id}&arrival_id=${arrival_id}&outbound_date=${outbound_date}&currency=${currency}&hl=${hl}`;

    const response = await fetch(apiUrl);
    const data = await response.json();

    res.json(data);
  } catch (error) {
    console.error('Error:', error);
    res.status(500).json({ error: 'Internal Server Error' });
  }
});

// Start the server
app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});
