import React from 'react';
import RangeSlider from 'react-bootstrap-range-slider';

// Props interface for the BuildForm component
interface BuildFormProps {
  purpose: string;
  budget: number;
  priorities: string; // Add priorities prop
  preferredBrands: string[];
  loading: boolean;
  onPurposeChange: (purpose: string) => void;
  onBudgetChange: (budget: number) => void;
  onPrioritiesChange: (priorities: string) => void; // Add priorities handler
  onPreferredBrandsChange: (brands: string[]) => void;
  onSubmit: (e: React.FormEvent) => void;
}

// Available brands list
const AVAILABLE_BRANDS = [
  "AMD",
  "NVIDIA",
  "Intel",
  "ASUS",
  "MSI",
  "Gigabyte",
  "EVGA",
  "Corsair",
  "G.Skill",
  "Samsung",
  "Western Digital",
  "Seagate",
  "Seasonic",
  "NZXT",
  "Cooler Master",
];

// BuildForm component for user input
export const BuildForm: React.FC<BuildFormProps> = ({
  purpose,
  budget,
  priorities, // Add priorities prop
  preferredBrands,
  loading,
  onPurposeChange,
  onBudgetChange,
  onPrioritiesChange, // Add priorities handler
  onPreferredBrandsChange,
  onSubmit,
}) => {
  const handlePurposeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    onPurposeChange(e.target.value);
  };

  const handlePrioritiesChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    onPrioritiesChange(e.target.value);
  };

  const handleBudgetChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    if (value === "") {
      onBudgetChange(0);
    } else {
      const numValue = parseInt(value);
      if (!isNaN(numValue)) {
        onBudgetChange(numValue);
      }
    }
  };

  const handleBrandChange = (brand: string) => {
    if (preferredBrands.includes(brand)) {
      // Remove brand if already selected
      onPreferredBrandsChange(preferredBrands.filter((b) => b !== brand));
    } else {
      // Add brand if not selected
      onPreferredBrandsChange([...preferredBrands, brand]);
    }
  };

  const handleSelectAll = () => {
    if (preferredBrands.length === AVAILABLE_BRANDS.length) {
      onPreferredBrandsChange([]);
    } else {
      onPreferredBrandsChange([...AVAILABLE_BRANDS]);
    }
  };

  return (
    <form onSubmit={onSubmit} className="build-form">
      <div className="form-group">
        <label htmlFor="purpose">Build Purpose:</label>
        <select
          id="purpose"
          value={purpose}
          onChange={handlePurposeChange}
          disabled={loading}
          className="form-input"
        >
          <option value="Gaming">Gaming & Creative Work</option>
          <option value="Productivity">Productivity & Development</option>
        </select>
      </div>

      <div className="form-group">
        <label htmlFor="priorities">Priorities:</label>
        <select
          id="priorities"
          value={priorities}
          onChange={handlePrioritiesChange}
          disabled={loading}
          className="form-input"
        >
          <option value="">Select your priority...</option>
          <option value="Performance">
            Performance (Best specs for the money)
          </option>
          <option value="Silence">Silence (Quiet operation)</option>
          <option value="Aesthetics">Aesthetics (RGB & visual appeal)</option>
          <option value="Upgradability">
            Upgradability (Future-proof components)
          </option>
          <option value="Efficiency">
            Power Efficiency (Low power consumption)
          </option>
          <option value="Compact">Compact Size (Small form factor)</option>
        </select>
      </div>

      <div className="form-group">
        <label htmlFor="budget">Budget (USD):</label>
        <input
          type="number"
          id="budget"
          value={budget || ""}
          onChange={handleBudgetChange}
          min="500"
          max="10000"
          disabled={loading}
          className="form-input"
          placeholder="Enter your budget"
        />
        <RangeSlider
          min={500}
          max={10000}
          step={5}
          value={budget}
          onChange={e => onBudgetChange(Number(e.target.value))}
          disabled={loading}
          tooltip="auto"
        />
      </div>

      <div className="form-group">
        <label>Preferred Brands:</label>
        <div className="brands-container">
          <div className="select-all-container">
            <span className="selected-count">
              {preferredBrands.length} of {AVAILABLE_BRANDS.length} selected
            </span>
            <button
              type="button"
              onClick={handleSelectAll}
              className="select-all-btn"
              disabled={loading}
            >
              {preferredBrands.length === AVAILABLE_BRANDS.length
                ? "Deselect All"
                : "Select All"}
            </button>
          </div>
          <div className="brands-grid">
            {AVAILABLE_BRANDS.map((brand) => (
              <label key={brand} className="brand-checkbox">
                <input
                  type="checkbox"
                  checked={preferredBrands.includes(brand)}
                  onChange={() => handleBrandChange(brand)}
                  disabled={loading}
                />
                <span className="checkmark"></span>
                <span className="brand-name">{brand}</span>
              </label>
            ))}
          </div>
        </div>
      </div>

      <div className="submit-button-container">
        <button type="submit" className="btn btn-primary" disabled={loading}>
          {loading ? "Building..." : "Build PC"}
        </button>
      </div>
    </form>
  );
};
