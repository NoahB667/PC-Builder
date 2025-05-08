import React from "react";

// Props interface for the BuildForm component
interface BuildFormProps {
  purpose: string; // Current selected purpose
  budget: number; // Current budget value
  loading: boolean; // Whether the form is in loading state
  onPurposeChange: (purpose: string) => void; // Handler for changing purpose
  onBudgetChange: (budget: number) => void; // Handler for changing budget
  onSubmit: (e: React.FormEvent) => void; // Handler for form submission
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
        <label htmlFor="budget">Budget ($):</label>
        <input
          type="number"
          id="budget"
          value={budget || ""}
          onChange={handleBudgetChange}
          min="0"
          disabled={loading}
          className="form-input"
          placeholder="Enter your budget"
        />
      </div>

      <button type="submit" className="btn btn-primary">Build PC</button>
    </form>
  );
};
