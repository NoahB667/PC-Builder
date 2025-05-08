import { useState, useEffect } from "react";
import "../App.css";
import { apiUrl } from "../App";

interface Component {
  id: number;
  brand: string;
  name: string;
  socket: string;
  powerWatt: number;
  price: number;
  performanceScore: number;
}

const AvailableComponents = () => {
  const [selectedType, setSelectedType] = useState<string>("cpu");
  const [components, setComponents] = useState<Component[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const fetchComponents = async (type: string) => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`${apiUrl}/api/components/${type}`);
      if (!response.ok) {
        throw new Error("Failed to fetch components");
      }
      const data: Component[] = await response.json();
      setComponents(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : "An error occurred");
    } finally {
      setLoading(false);
    }
  };

  const handleTypeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const type = e.target.value;
    setSelectedType(type);
    fetchComponents(type);
  };

  useEffect(() => {
    fetchComponents(selectedType);
  }, []);

  return (
    <div className="page-container">
      <h1>Available Components</h1>
      <p>Select a component type to view all available options:</p>
      <div className="form-group">
        <label htmlFor="component-type">Component Type:</label>
        <select
          id="component-type"
          value={selectedType}
          onChange={handleTypeChange}
          className="form-input"
        >
          <option value="cpu">CPU</option>
          <option value="gpu">GPU</option>
          <option value="motherboard">Motherboard</option>
          <option value="ram">RAM</option>
          <option value="storage">Storage</option>
          <option value="psu">Power Supply (PSU)</option>
          <option value="case">Case</option>
        </select>
      </div>

      {loading && <p>Loading components...</p>}
      {error && <p className="error-message">{error}</p>}

      {!loading && !error && components.length > 0 && (
        <table className="table table-striped table-hover mt-4">
          <thead className="table-dark">
            <tr>
              <th>Name</th>
              <th>Brand</th>
              <th>Price ($)</th>
              <th>Performance Score</th>
            </tr>
          </thead>
          <tbody>
            {components.map((component) => (
              <tr key={component.id}>
                <td>{component.name}</td>
                <td>{component.brand}</td>
                <td>{component.price.toFixed(2)}</td>
                <td>{component.performanceScore}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {!loading && !error && components.length === 0 && (
        <p>No components found for the selected type.</p>
      )}
    </div>
  );
};

export default AvailableComponents;
