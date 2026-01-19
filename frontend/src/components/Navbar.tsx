import React from "react";
import "./Navbar.css";

const Navbar: React.FC = () => {
  return (
    <nav className="navbar">
      <div className="nav-container">
        <div className="nav-content">
          <div className="nav-brand">PC Build Advisor</div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
