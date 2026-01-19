import React from "react";

interface BuildResultProps {
  components: {
    [key: string]: {
      name: string;
      price: number;
    };
  };
  totalPrice: number;
  score: number;
}

export const BuildResult: React.FC<BuildResultProps> = ({
  components,
  totalPrice,
  score,
}) => {
  const renderedComponents = Object.entries(components).map(([key, value]) => (
    <div key={key} className="component-item">
      <span className="component-type">{key}</span>
      <span className="component-name">{value.name}</span>
      <span className="component-price">${value.price.toFixed(2)}</span>
    </div>
  ));

  return (
    <div className="build-result">
      <h2>Recommended Build</h2>
      <div className="build-details">
        <div className="build-stats">
          <p>
            Total Price: <span>${totalPrice.toFixed(2)}</span>
          </p>
          <p>
            Build Score: <span>{score}</span>
          </p>
        </div>
        <div className="components-list">{renderedComponents}</div>
      </div>
    </div>
  );
};
