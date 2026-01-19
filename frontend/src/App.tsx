import { useState } from "react";
import "./App.css";
import { BuildForm } from "./components/BuildForm";
import { BuildResult } from "./components/BuildResult";
import Navbar from "./components/Navbar";

export const apiUrl =
  import.meta.env.VITE_API_URL || "http://localhost:8080";

// TypeScript interface for a single component
interface Component {
  id: number;
  brand: string;
  name: string;
  price: number;
  performanceScore: number;
}

// Interface for the backend build suggestion response
interface BuildSuggestion {
  components: {
    cpu: Component | null;
    motherboard: Component | null;
    ram: Component | null;
    gpu: Component | null;
    storage: Component | null;
    psu: Component | null;
    case: Component | null;
  };
  totalCost: number;
  score: number;
  error?: string;
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
  const [purpose, setPurpose] = useState<string>("Gaming");
  const [budget, setBudget] = useState<number>(1000);
  const [loading, setLoading] = useState<boolean>(false);
  const [buildResult, setBuildResult] = useState<FormattedBuildResult | null>(
    null
  );
  const [error, setError] = useState<string | null>(null);

  // Format the backend build suggestion into a frontend-friendly structure
  const formatBuildResult = (
    suggestion: BuildSuggestion
  ): FormattedBuildResult => {
    const formattedComponents: {
      [key: string]: { name: string; price: number };
    } = {};

    // Map backend component keys to display names
    const componentMapping: { [key: string]: string } = {
      cpu: "CPU",
      gpu: "GPU",
      motherboard: "Motherboard",
      ram: "RAM",
      storage: "Storage",
      psu: "PSU",
      case: "Case",
    };

    // Format all components from the object
    Object.entries(suggestion.components).forEach(([key, component]) => {
      if (component) {
        const displayName = componentMapping[key] || key;
        formattedComponents[displayName] = {
          name: `${component.brand} ${component.name}`,
          price: component.price,
        };
      }
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

    return {
      components: orderedComponents,
      totalPrice: suggestion.totalCost,
      score: suggestion.score,
    };
  };

  // Handle form submission to get build suggestion from backend
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
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
        throw new Error(errorData?.error || errorData?.message || "Failed to get build suggestion");
      }

      const result: BuildSuggestion = await response.json();

      // Check if backend returned an error
      if (result.error) {
        throw new Error(result.error);
      }

      setBuildResult(formatBuildResult(result));
    } catch (err) {
      setError(err instanceof Error ? err.message : "An error occurred");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app-container">
      <Navbar />
      <div className="main-content">
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
          <div className="error-message">{error}</div>
        )}
        {buildResult && (
          <div className="result-container">
            <BuildResult {...buildResult} />
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
