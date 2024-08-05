// src/Header.js
import React from 'react';
import './Header.css';

const Header = ({ onLogout }) => {
  return (
    <div className="header">
      <div className="header-title">SkyScanner</div>
      <div className="header-right">
        <button className="logout-button" onClick={onLogout}>Logout</button>
      </div>
    </div>
  );
};

export default Header;
