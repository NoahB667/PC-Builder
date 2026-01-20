export interface BaseComponent {
  id: number;
  price: number;
}

export interface CpuComponent extends BaseComponent {
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

export interface GpuComponent extends BaseComponent {
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

export interface MotherboardComponent extends BaseComponent {
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

export interface RamComponent extends BaseComponent {
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

export interface StorageComponent extends BaseComponent {
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

export interface PsuComponent extends BaseComponent {
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

export interface CaseComponent extends BaseComponent {
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

export type Component =
  | CpuComponent
  | GpuComponent
  | MotherboardComponent
  | RamComponent
  | StorageComponent
  | PsuComponent
  | CaseComponent;

export interface BuildSuggestion {
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

export interface FormattedBuildResult {
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

