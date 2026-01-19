import { useState } from "react";
import "./App.css";
import { BuildForm } from "./components/BuildForm";
import { BuildResult } from "./components/BuildResult";
import Navbar from "./components/Navbar";

export const apiUrl =
  import.meta.env.VITE_API_URL || "http://localhost:8080";

// Base interface for common component properties
interface BaseComponent {
  id: number;
  price: number;
}

// CPU specific interface
interface CpuComponent extends BaseComponent {
  brand: string;
  name: string;
  socket: string;
  cores: number;
  threads: number;
  baseClock: number;
  boostClock: number;
  tdp: number;
  graphics?: string;
}

// GPU specific interface - matches Gpu.java
interface GpuComponent extends BaseComponent {
  manufacturer: string;
  modelName: string;
  chipset_brand: string;
  vramGb: number;
  vramType: string;
  boostClockMhz: number;
  performanceScore: number;
  tdp: number;
  lengthMm: number;
  slotWidth: number;
  pcieGen: number;
}

// Motherboard specific interface - matches Motherboard.java
interface MotherboardComponent extends BaseComponent {
  brand: string;
  modelName: string;
  socket: string;
  chipset: string;
  formFactor: string;
  ramGen: string;
  ramSlots: number;
  maxRamSpeedMts?: number;
  pcieGenPrimary?: string;
  m2SlotsCount?: number;
  hasWifi?: boolean;
  wifiVersion?: string;
  supportsBackConnect?: boolean;
}

// RAM specific interface - matches Ram.java
interface RamComponent extends BaseComponent {
  brand: string;
  name: string; // Note: backend uses 'name' not 'modelName'
  generation: string;
  speedMhz: number;
  casLatency: number;
  totalCapacityGb: number;
  numModules: number;
  isExpo: boolean;
  isXmp: boolean;
  heightMm: string;
}

// Storage specific interface - matches Storage.java
interface StorageComponent extends BaseComponent {
  brand: string;
  modelName: string;
  capacityGb: number;
  interfaceType: string;
  formFactor: string;
  maxReadSpeedMbs: number;
  maxWriteSpeedMbs: number;
  tbwRating: number;
  hasDram: boolean;
  nandType: string;
  includesHeatSink: boolean;
}

// PSU specific interface - matches Psu.java
interface PsuComponent extends BaseComponent {
  brand: string;
  modelName: string;
  wattage: number;
  efficiencyRating: string;
  modularity: string;
  formFactor: string;
  atxVersion?: string;
  has12v2x6?: boolean;
  pcie51Ready?: boolean;
}

// Case specific interface - matches Case.java
interface CaseComponent extends BaseComponent {
  brand: string;
  modelName: string;
  color?: string;
  type?: string;
  maxMotherboardSize?: string;
  psuFormFactor?: string;
  maxGpuLengthMm?: number;
  maxCpuCoolerHeightMm?: number;
  maxRadiatorSupportMm?: number;
  hasTemperedGlass?: boolean;
  supportsBackConnect?: boolean;
  usbCFrontPanel?: boolean;
}

// Union type for all component types
type Component = CpuComponent | GpuComponent | MotherboardComponent | RamComponent | StorageComponent | PsuComponent | CaseComponent;

// Interface for the backend build suggestion response
interface BuildSuggestion {
  components: {
    cpu: CpuComponent | null;
    motherboard: MotherboardComponent | null;
    ram: RamComponent | null;
    gpu: GpuComponent | null;
    storage: StorageComponent | null;
    psu: PsuComponent | null;
    case: CaseComponent | null;
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
      details: Component;
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
      [key: string]: { name: string; price: number; details: Component };
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

    // Helper function to get the display name for a component
    const getComponentDisplayName = (key: string, component: Component): string => {
      switch (key) {
        case "cpu": {
          const cpu = component as CpuComponent;
          return cpu.name.trim();
        }
        case "gpu": {
          const gpu = component as GpuComponent;
          return gpu.modelName.trim();
        }
        case "motherboard": {
          const mobo = component as MotherboardComponent;
          return `${mobo.brand} ${mobo.modelName}`.trim();
        }
        case "ram": {
          const ram = component as RamComponent;
          return ram.name.trim();
        }
        case "storage": {
          const storage = component as StorageComponent;
          return `${storage.brand} ${storage.modelName}`.trim();
        }
        case "psu": {
          const psu = component as PsuComponent;
          return `${psu.brand} ${psu.modelName}`.trim();
        }
        case "case": {
          const caseComp = component as CaseComponent;
          return `${caseComp.brand} ${caseComp.modelName}`.trim();
        }
        default:
          return "Unknown Component";
      }
    };

    // Format all components from the object
    Object.entries(suggestion.components).forEach(([key, component]) => {
      if (component) {
        const displayName = componentMapping[key] || key;
        formattedComponents[displayName] = {
          name: getComponentDisplayName(key, component),
          price: component.price,
          details: component,
        };
      }
    });

    // Order components according to COMPONENT_ORDER
    const orderedComponents: {
      [key: string]: { name: string; price: number; details: Component };
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
