import React from "react";
import { Link } from "react-router-dom";
import "./Navbar.css";

interface NavbarProps {
  variant?: "landing" | "default";
}

const Navbar: React.FC<NavbarProps> = ({ variant = "default" }) => {
  return (
    <nav
      className={`navbar ${variant === "landing" ? "landing-nav" : "app-nav"}`}
    >
      <div className="nav-container">
        <div className="nav-content">
          <Link to="/" className="nav-brand">
            <span></span>
            PC Build Advisor
          </Link>
          <div className="nav-links">
            <Link to="/description" className="nav-link">
              Components
            </Link>
            <Link to="/available-components" className="nav-link">
              Browse
            </Link>
            <Link to="/builder" className="nav-link">
              Builder
            </Link>
            <button className="nav-login-btn">Log In</button>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
