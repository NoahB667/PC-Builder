import { useState } from "react";
import "./App.css";
import { BuildForm } from "./components/BuildForm";
import { BuildResult } from "./components/BuildResult";
import Navbar from "./components/Navbar";

export const apiUrl =
  import.meta.env.VITE_API_URL || "http://localhost:8080";

interface BaseComponent {
  id: number;
  price: number;
}

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

interface RamComponent extends BaseComponent {
  brand: string;
  name: string;
  generation: string;
  speedMhz: number;
  casLatency: number;
  totalCapacityGb: number;
  numModules: number;
  isExpo: boolean;
  isXmp: boolean;
  heightMm: string;
}

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

type Component = CpuComponent | GpuComponent | MotherboardComponent | RamComponent | StorageComponent | PsuComponent | CaseComponent;

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

  const formatBuildResult = (
    suggestion: BuildSuggestion
  ): FormattedBuildResult => {
    const formattedComponents: {
      [key: string]: { name: string; price: number; details: Component };
    } = {};

    const componentMapping: { [key: string]: string } = {
      cpu: "CPU",
      gpu: "GPU",
      motherboard: "Motherboard",
      ram: "RAM",
      storage: "Storage",
      psu: "PSU",
      case: "Case",
    };

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

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const params = new URLSearchParams({
        purpose: purpose,
        budget: budget.toString(),
      });
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
