import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Navbar from "./Navbar";
import "./LandingPage.css";

const LandingPage: React.FC = () => {
  const [currentSlide, setCurrentSlide] = useState(0);

  const pcBuilds = [
    {
      id: 1,
      title: "RGB Gaming Beast",
      description: "High-performance gaming with stunning RGB lighting",
      image: "/rgb-gaming-pc.png",
      specs: ["RTX 4080", "Intel i7-13700K", "32GB DDR5", "1TB NVMe SSD"],
    },
    {
      id: 2,
      title: "Honeycomb RGB Pro",
      description: "Premium components with custom cooling solutions",
      image: "/honeycomb-rgb-pc.png",
      specs: ["RTX 4090", "AMD Ryzen 9 7900X", "64GB DDR5", "2TB NVMe SSD"],
    },
    {
      id: 3,
      title: "Stealth Performance",
      description: "Sleek design with maximum performance",
      image: "/rgb-gaming-pc.png",
      specs: ["RTX 4070", "Intel i5-13600K", "16GB DDR5", "512GB NVMe SSD"],
    },
    {
      id: 4,
      title: "Creator's Dream",
      description: "Optimized for content creation and streaming",
      image: "/honeycomb-rgb-pc.png",
      specs: ["RTX 4080", "AMD Ryzen 9 7950X", "128GB DDR5", "4TB NVMe SSD"],
    },
  ];

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % pcBuilds.length);
    }, 4000); // Auto-advance every 4 seconds

    return () => clearInterval(timer);
  }, [pcBuilds.length]);

  const goToSlide = (index: number) => {
    setCurrentSlide(index);
  };
  return (
    <div className="landing-page">
      {/* Navigation Bar */}
      <Navbar variant="landing" />

      {/* Background Pattern */}
      <div className="background-pattern"></div>

      {/* Main Content */}
      <div className="hero-container">
        {/* Hero Content */}
        <div className="hero-content">
          <h2 className="hero-title">Optimize your next PC build</h2>
          <p className="hero-subtitle">
            Get recommendations based on your budget, use case, and
            compatibility.
          </p>

          {/* Call-to-Action Buttons */}
          <div className="cta-buttons">
            <Link to="/builder" className="btn btn-primary">
              Start Building
            </Link>
            <Link to="/description" className="btn btn-secondary">
              Learn More
            </Link>
          </div>
        </div>

        {/* PC Showcase Slideshow */}
        <div className="pc-slideshow-aligned">
          <div className="slideshow-container-aligned">
            <div
              className="slide-wrapper-aligned"
              style={{ transform: `translateX(-${currentSlide * 100}%)` }}
            >
              {pcBuilds.map((build) => (
                <div key={build.id} className="slide-aligned">
                  <div className="slide-background-aligned">
                    <img src={build.image} alt={build.title} />
                  </div>
                  <div className="slide-content-overlay-aligned">
                    <div className="slide-text-content-aligned">
                      <h3>{build.title}</h3>
                      <p>{build.description}</p>
                      <div className="slide-specs-aligned">
                        {build.specs.map((spec, specIndex) => (
                          <span key={specIndex} className="spec-tag-aligned">
                            {spec}
                          </span>
                        ))}
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            {/* Slide Indicators */}
            <div className="slide-indicators-aligned">
              {pcBuilds.map((_, slideIndex) => (
                <button
                  key={slideIndex}
                  className={`indicator-aligned ${
                    slideIndex === currentSlide ? "active" : ""
                  }`}
                  onClick={() => goToSlide(slideIndex)}
                />
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LandingPage;
