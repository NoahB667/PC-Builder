import React from 'react';
import RangeSlider from 'react-bootstrap-range-slider';

// Props interface for the BuildForm component
interface BuildFormProps {
  purpose: string;
  budget: number;
  loading: boolean;
  onPurposeChange: (purpose: string) => void;
  onBudgetChange: (budget: number) => void;
  onSubmit: (e: React.FormEvent) => void;
}

// BuildForm component for user input
export const BuildForm: React.FC<BuildFormProps> = ({
  purpose,
  budget,
  loading,
  onPurposeChange,
  onBudgetChange,
  onSubmit,
}) => {
  const handlePurposeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    onPurposeChange(e.target.value);
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
        <label htmlFor="budget">Budget (USD):</label>
        <input
          type="number"
          id="budget"
          value={budget || ""}
          onChange={handleBudgetChange}
          min="500"
          max="5000"
          disabled={loading}
          className="form-input"
          placeholder="Enter your budget"
        />
        <RangeSlider
          min={500}
          max={5000}
          step={5}
          value={budget}
          onChange={e => onBudgetChange(Number(e.target.value))}
          disabled={loading}
          tooltip="auto"
        />
      </div>

      <div className="submit-button-container">
        <button type="submit" className="btn btn-primary" disabled={loading}>
          {loading ? "Building..." : "Build PC"}
        </button>
      </div>
    </form>
  );
};
