import React, { useState } from "react";

// Base interface for common component properties
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

interface BuildResultProps {
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

export const BuildResult: React.FC<BuildResultProps> = ({
  components,
  totalPrice,
  score,
}) => {
  const [expandedComponent, setExpandedComponent] = useState<string | null>(null);

  const toggleComponent = (key: string) => {
    setExpandedComponent(expandedComponent === key ? null : key);
  };

  const renderComponentDetails = (key: string, details: Component) => {
    const detailsMap: { [label: string]: string | number } = {};

    switch (key) {
      case "CPU": {
        const cpu = details as CpuComponent;
        if (cpu.socket) detailsMap["Socket"] = cpu.socket;
        if (cpu.cores) detailsMap["Cores"] = cpu.cores;
        if (cpu.threads) detailsMap["Threads"] = cpu.threads;
        if (cpu.baseClock) detailsMap["Base Clock"] = `${cpu.baseClock} GHz`;
        if (cpu.boostClock) detailsMap["Boost Clock"] = `${cpu.boostClock} GHz`;
        if (cpu.tdp) detailsMap["TDP"] = `${cpu.tdp}W`;
        if (cpu.graphics) detailsMap["Integrated Graphics"] = cpu.graphics;
        break;
      }
      case "GPU": {
        const gpu = details as GpuComponent;
        if (gpu.chipset_brand) detailsMap["Chipset"] = gpu.chipset_brand;
        if (gpu.vramGb) detailsMap["VRAM"] = `${gpu.vramGb}GB ${gpu.vramType}`;
        if (gpu.boostClockMhz) detailsMap["Boost Clock"] = `${gpu.boostClockMhz} MHz`;
        if (gpu.tdp) detailsMap["TDP"] = `${gpu.tdp}W`;
        if (gpu.lengthMm) detailsMap["Length"] = `${gpu.lengthMm}mm`;
        if (gpu.slotWidth) detailsMap["Slot Width"] = `${gpu.slotWidth} slots`;
        if (gpu.pcieGen) detailsMap["PCIe Gen"] = gpu.pcieGen;
        if (gpu.performanceScore) detailsMap["Performance Score"] = gpu.performanceScore;
        break;
      }
      case "Motherboard": {
        const mobo = details as MotherboardComponent;
        if (mobo.chipset) detailsMap["Chipset"] = mobo.chipset;
        if (mobo.socket) detailsMap["Socket"] = mobo.socket;
        if (mobo.formFactor) detailsMap["Form Factor"] = mobo.formFactor;
        if (mobo.ramGen) detailsMap["RAM Type"] = mobo.ramGen;
        if (mobo.ramSlots) detailsMap["RAM Slots"] = mobo.ramSlots;
        if (mobo.maxRamSpeedMts) detailsMap["Max RAM Speed"] = `${mobo.maxRamSpeedMts} MT/s`;
        if (mobo.pcieGenPrimary) detailsMap["PCIe Gen"] = mobo.pcieGenPrimary;
        if (mobo.m2SlotsCount) detailsMap["M.2 Slots"] = mobo.m2SlotsCount;
        if (mobo.hasWifi) detailsMap["WiFi"] = mobo.wifiVersion || "Yes";
        if (mobo.supportsBackConnect) detailsMap["Back Connect"] = "Yes";
        break;
      }
      case "RAM": {
        const ram = details as RamComponent;
        if (ram.totalCapacityGb) detailsMap["Capacity"] = `${ram.totalCapacityGb}GB (${ram.numModules}x${ram.totalCapacityGb/ram.numModules}GB)`;
        if (ram.generation) detailsMap["Generation"] = ram.generation;
        if (ram.speedMhz) detailsMap["Speed"] = `${ram.speedMhz} MHz`;
        if (ram.casLatency) detailsMap["CAS Latency"] = `CL${ram.casLatency}`;
        if (ram.heightMm) detailsMap["Height"] = ram.heightMm;
        if (ram.isXmp || ram.isExpo) {
          const overclocking = [];
          if (ram.isXmp) overclocking.push("XMP");
          if (ram.isExpo) overclocking.push("EXPO");
          detailsMap["Overclocking"] = overclocking.join(" / ");
        }
        break;
      }
      case "Storage": {
        const storage = details as StorageComponent;
        if (storage.capacityGb) detailsMap["Capacity"] = `${storage.capacityGb}GB`;
        if (storage.interfaceType) detailsMap["Interface"] = storage.interfaceType;
        if (storage.formFactor) detailsMap["Form Factor"] = storage.formFactor;
        if (storage.maxReadSpeedMbs) detailsMap["Read Speed"] = `${storage.maxReadSpeedMbs} MB/s`;
        if (storage.maxWriteSpeedMbs) detailsMap["Write Speed"] = `${storage.maxWriteSpeedMbs} MB/s`;
        if (storage.nandType) detailsMap["NAND Type"] = storage.nandType;
        if (storage.hasDram) detailsMap["DRAM Cache"] = "Yes";
        if (storage.includesHeatSink) detailsMap["Heatsink"] = "Included";
        if (storage.tbwRating) detailsMap["Endurance"] = `${storage.tbwRating} TBW`;
        break;
      }
      case "PSU": {
        const psu = details as PsuComponent;
        if (psu.wattage) detailsMap["Wattage"] = `${psu.wattage}W`;
        if (psu.efficiencyRating) detailsMap["Efficiency"] = psu.efficiencyRating;
        if (psu.modularity) detailsMap["Modularity"] = psu.modularity;
        if (psu.formFactor) detailsMap["Form Factor"] = psu.formFactor;
        if (psu.atxVersion) detailsMap["ATX Version"] = psu.atxVersion;
        if (psu.has12v2x6) detailsMap["12V-2x6 Connector"] = "Yes";
        if (psu.pcie51Ready) detailsMap["PCIe 5.1 Ready"] = "Yes";
        break;
      }
      case "Case": {
        const caseComp = details as CaseComponent;
        if (caseComp.type) detailsMap["Type"] = caseComp.type;
        if (caseComp.color) detailsMap["Color"] = caseComp.color;
        if (caseComp.maxMotherboardSize) detailsMap["Max Motherboard"] = caseComp.maxMotherboardSize;
        if (caseComp.psuFormFactor) detailsMap["PSU Form Factor"] = caseComp.psuFormFactor;
        if (caseComp.maxGpuLengthMm) detailsMap["Max GPU Length"] = `${caseComp.maxGpuLengthMm}mm`;
        if (caseComp.maxCpuCoolerHeightMm) detailsMap["Max CPU Cooler Height"] = `${caseComp.maxCpuCoolerHeightMm}mm`;
        if (caseComp.maxRadiatorSupportMm) detailsMap["Max Radiator"] = `${caseComp.maxRadiatorSupportMm}mm`;
        if (caseComp.hasTemperedGlass) detailsMap["Tempered Glass"] = "Yes";
        if (caseComp.supportsBackConnect) detailsMap["Back Connect"] = "Yes";
        if (caseComp.usbCFrontPanel) detailsMap["USB-C Front Panel"] = "Yes";
        break;
      }
    }

    return (
      <div className="component-details">
        {Object.entries(detailsMap).map(([label, value]) => (
          <div key={label} className="detail-row">
            <span className="detail-label">{label}:</span>
            <span className="detail-value">{value}</span>
          </div>
        ))}
      </div>
    );
  };

  const renderedComponents = Object.entries(components).map(([key, value]) => (
    <div key={key} className="component-wrapper">
      <div
        className="component-item"
        onClick={() => toggleComponent(key)}
        style={{ cursor: 'pointer' }}
      >
        <span className="component-type">{key}</span>
        <span className="component-name">{value.name}</span>
        <span className="component-price">${value.price.toFixed(2)}</span>
        <span className="expand-icon">{expandedComponent === key ? '▼' : '▶'}</span>
      </div>
      {expandedComponent === key && renderComponentDetails(key, value.details)}
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

