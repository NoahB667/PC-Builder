import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import { useState } from "react";
import "./App.css";
import { BuildForm } from "./components/BuildForm";
import { BuildResult } from "./components/BuildResult";
import ComponentDescription from "./components/ComponentDescription";
import AvailableComponents from "./components/AvailableComponents";

export const apiUrl =
  import.meta.env.REACT_APP_API_URL ||
  `${window.location.protocol}//${window.location.hostname}:8080`;

// TypeScript interface for a single component
interface Component {
  id: number;
  type: string;
  brand: string;
  name: string;
  socket: string;
  powerWatt: number;
  price: number;
  performanceScore: number;
}

// Interface for the backend build suggestion response
interface BuildSuggestion {
  components: Component[];
  compatibilityPass: boolean;
  totalPrice: number;
}

// Interface for the formatted build result used in the frontend
interface FormattedBuildResult {
  components: {
    [key: string]: {
      name: string;
      price: number;
    };
  };
  totalPrice: number;
  score: number;
  isCompatible: boolean;
}

// Fixed order for displaying components in the result
const COMPONENT_ORDER = [
  "CPU",
  "GPU",
  "Motherboard",
  "RAM",
  "Storage",
  "PSU",
  "Case",
];

function App() {
  // State for build purpose, budget, loading, result, and error
  const [purpose, setPurpose] = useState<string>("Gaming");
  const [budget, setBudget] = useState<number>(1000);
  const [loading, setLoading] = useState<boolean>(false);
  const [buildResult, setBuildResult] = useState<FormattedBuildResult | null>(
    null
  );
  const [error, setError] = useState<string | null>(null);

  // Helper to format component type for display
  const formatComponentType = (type: string): string => {
    switch (type.toLowerCase()) {
      case "cpu":
        return "CPU";
      case "gpu":
        return "GPU";
      case "ram":
        return "RAM";
      case "power supply":
      case "psu":
        return "PSU";
      default:
        return type.charAt(0).toUpperCase() + type.slice(1).toLowerCase();
    }
  };

  // Format the backend build suggestion into a frontend-friendly structure
  const formatBuildResult = (
    suggestion: BuildSuggestion
  ): FormattedBuildResult => {
    const formattedComponents: {
      [key: string]: { name: string; price: number };
    } = {};

    // Format all components by type
    suggestion.components.forEach((component) => {
      const formattedType = formatComponentType(component.type);
      formattedComponents[formattedType] = {
        name: `${component.brand} ${component.name}`,
        price: component.price,
      };
    });

    // Order components according to COMPONENT_ORDER
    const orderedComponents: {
      [key: string]: { name: string; price: number };
    } = {};
    COMPONENT_ORDER.forEach((type) => {
      if (formattedComponents[type]) {
        orderedComponents[type] = formattedComponents[type];
      }
    });

    // Calculate average performance score
    const averageScore = Math.round(
      suggestion.components.reduce(
        (acc, curr) => acc + curr.performanceScore,
        0
      ) / suggestion.components.length
    );

    return {
      components: orderedComponents,
      totalPrice: suggestion.totalPrice,
      score: averageScore,
      isCompatible: suggestion.compatibilityPass,
    };
  };

  // Handle form submission to get build suggestion from backend
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      // Prepare query params
      const params = new URLSearchParams({
        purpose: purpose,
        budget: budget.toString(),
      });

      // Fetch build suggestion from backend
      const response = await fetch(
        `${apiUrl}/api/components/build/suggest?${params}`,
        {
          method: "GET",
          headers: {
            Accept: "application/json",
          },
        }
      );

      if (!response.ok) {
        const errorData = await response.json().catch(() => null);
        throw new Error(errorData?.message || "Failed to get build suggestion");
      }

      const result: BuildSuggestion = await response.json();

      if (!result.components || result.components.length === 0) {
        throw new Error("No compatible components found for your requirements");
      }

      setBuildResult(formatBuildResult(result));
    } catch (err) {
      setError(err instanceof Error ? err.message : "An error occurred");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Router>
      <div className="container">
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
          <div className="container">
            <Link className="navbar-brand" to="/">
              PC Build Advisor
            </Link>
            <div className="collapse navbar-collapse" id="navbarNav">
              <ul className="navbar-nav ms-auto">
                <li className="nav-item">
                  <Link className="nav-link" to="/description">
                    Component Descriptions
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/available-components">
                    Available Components
                  </Link>
                </li>
              </ul>
            </div>
          </div>
        </nav>

        <Routes>
          <Route
            path="/"
            element={
              <div className="container mt-4 row col-md-8">
                <h1>PC Build Advisor</h1>
                <BuildForm
                  purpose={purpose}
                  budget={budget}
                  loading={loading}
                  onPurposeChange={setPurpose}
                  onBudgetChange={setBudget}
                  onSubmit={handleSubmit}
                />
                {error && (
                  <div className="alert alert-danger mt-3">{error}</div>
                )}
                <div className="container mt-4 row col-md-8">
                  {buildResult && <BuildResult {...buildResult} />}
                </div>
              </div>
            }
          />
          <Route path="/description" element={<ComponentDescription />} />
          <Route
            path="/available-components"
            element={<AvailableComponents />}
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
