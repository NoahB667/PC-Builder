package com.pcbuilder.service;

import com.pcbuilder.entity.*;
import com.pcbuilder.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BuildSuggestionService {

    @Autowired
    private CpuRepository cpuRepository;

    @Autowired
    private GpuRepository gpuRepository;

    @Autowired
    private MotherboardRepository motherboardRepository;

    @Autowired
    private RamRepository ramRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private PsuRepository psuRepository;

    @Autowired
    private CaseRepository caseRepository;

    public Map<String, Object> suggestBuild(String purpose, Double budget, String preferredBrand) {
        Map<String, Object> suggestion = new HashMap<>();

        // Budget allocation based on purpose
        BudgetAllocation allocation = getBudgetAllocation(purpose, budget);

        // Filter components by preferred brand if specified
        List<Gpu> availableGpus = gpuRepository.findAll();
        List<Cpu> availableCpus = cpuRepository.findAll();

        if (preferredBrand != null && !preferredBrand.isEmpty()) {
            if (preferredBrand.equalsIgnoreCase("NVIDIA")) {
                availableGpus = availableGpus.stream()
                    .filter(gpu -> gpu.getBrand().equalsIgnoreCase("NVIDIA"))
                    .collect(Collectors.toList());
            } else if (preferredBrand.equalsIgnoreCase("AMD")) {
                availableGpus = availableGpus.stream()
                    .filter(gpu -> gpu.getBrand().equalsIgnoreCase("AMD"))
                    .collect(Collectors.toList());
                availableCpus = availableCpus.stream()
                    .filter(cpu -> cpu.getBrand().equalsIgnoreCase("AMD"))
                    .collect(Collectors.toList());
            }
        }

        // Find best components within budget
        Gpu selectedGpu = findBestGpu(availableGpus, allocation.gpuBudget);
        Cpu selectedCpu = findBestCpu(availableCpus, allocation.cpuBudget);
        Motherboard selectedMotherboard = findCompatibleMotherboard(selectedCpu, allocation.motherboardBudget);
        Ram selectedRam = findBestRam(allocation.ramBudget);
        Storage selectedStorage = findBestStorage(allocation.storageBudget);
        Psu selectedPsu = findSuitablePsu(selectedGpu, selectedCpu, allocation.psuBudget);
        Case selectedCase = findBestCase(allocation.caseBudget);

        // Calculate total cost
        double totalCost = 0;
        totalCost += selectedGpu != null ? selectedGpu.getPrice() : 0;
        totalCost += selectedCpu != null ? selectedCpu.getPrice() : 0;
        totalCost += selectedMotherboard != null ? selectedMotherboard.getPrice() : 0;
        totalCost += selectedRam != null ? selectedRam.getPrice() : 0;
        totalCost += selectedStorage != null ? selectedStorage.getPrice() : 0;
        totalCost += selectedPsu != null ? selectedPsu.getPrice() : 0;
        totalCost += selectedCase != null ? selectedCase.getPrice() : 0;

        // Build response
        suggestion.put("purpose", purpose);
        suggestion.put("budget", budget);
        suggestion.put("preferredBrand", preferredBrand);
        suggestion.put("totalCost", Math.round(totalCost * 100.0) / 100.0);
        suggestion.put("remainingBudget", Math.round((budget - totalCost) * 100.0) / 100.0);

        Map<String, Object> components = new HashMap<>();
        components.put("gpu", selectedGpu);
        components.put("cpu", selectedCpu);
        components.put("motherboard", selectedMotherboard);
        components.put("ram", selectedRam);
        components.put("storage", selectedStorage);
        components.put("psu", selectedPsu);
        components.put("case", selectedCase);

        suggestion.put("components", components);
        suggestion.put("buildNotes", generateBuildNotes(purpose, selectedGpu, selectedCpu, preferredBrand));

        return suggestion;
    }

    private BudgetAllocation getBudgetAllocation(String purpose, Double budget) {
        BudgetAllocation allocation = new BudgetAllocation();

        if ("Gaming".equalsIgnoreCase(purpose)) {
            // Gaming builds prioritize GPU and CPU
            allocation.gpuBudget = budget * 0.40;    // 40% for GPU
            allocation.cpuBudget = budget * 0.25;    // 25% for CPU
            allocation.motherboardBudget = budget * 0.10; // 10% for motherboard
            allocation.ramBudget = budget * 0.10;    // 10% for RAM
            allocation.storageBudget = budget * 0.08; // 8% for storage
            allocation.psuBudget = budget * 0.05;    // 5% for PSU
            allocation.caseBudget = budget * 0.02;   // 2% for case
        } else if ("Workstation".equalsIgnoreCase(purpose)) {
            // Workstation builds prioritize CPU and RAM
            allocation.cpuBudget = budget * 0.35;    // 35% for CPU
            allocation.gpuBudget = budget * 0.20;    // 20% for GPU
            allocation.ramBudget = budget * 0.20;    // 20% for RAM
            allocation.motherboardBudget = budget * 0.10; // 10% for motherboard
            allocation.storageBudget = budget * 0.10; // 10% for storage
            allocation.psuBudget = budget * 0.03;    // 3% for PSU
            allocation.caseBudget = budget * 0.02;   // 2% for case
        } else {
            // Office/General purpose - balanced build
            allocation.cpuBudget = budget * 0.30;    // 30% for CPU
            allocation.gpuBudget = budget * 0.15;    // 15% for GPU
            allocation.motherboardBudget = budget * 0.15; // 15% for motherboard
            allocation.ramBudget = budget * 0.15;    // 15% for RAM
            allocation.storageBudget = budget * 0.15; // 15% for storage
            allocation.psuBudget = budget * 0.05;    // 5% for PSU
            allocation.caseBudget = budget * 0.05;   // 5% for case
        }

        return allocation;
    }

    private Gpu findBestGpu(List<Gpu> gpus, Double budget) {
        return gpus.stream()
            .filter(gpu -> gpu.getPrice() <= budget)
            .max(Comparator.comparing(gpu -> gpu.getVram() * 1000 + (budget - gpu.getPrice())))
            .orElse(gpus.stream().min(Comparator.comparing(Gpu::getPrice)).orElse(null));
    }

    private Cpu findBestCpu(List<Cpu> cpus, Double budget) {
        return cpus.stream()
            .filter(cpu -> cpu.getPrice() <= budget)
            .max(Comparator.comparing(cpu -> cpu.getCores() * cpu.getThreads() * cpu.getBoostClock()))
            .orElse(cpus.stream().min(Comparator.comparing(Cpu::getPrice)).orElse(null));
    }

    private Motherboard findCompatibleMotherboard(Cpu cpu, Double budget) {
        if (cpu == null) return null;

        return motherboardRepository.findAll().stream()
            .filter(mb -> mb.getPrice() <= budget)
            .filter(mb -> mb.getSocket().equals(cpu.getSocket()))
            .min(Comparator.comparing(Motherboard::getPrice))
            .orElse(motherboardRepository.findAll().stream()
                .min(Comparator.comparing(Motherboard::getPrice))
                .orElse(null));
    }

    private Ram findBestRam(Double budget) {
        return ramRepository.findAll().stream()
            .filter(ram -> ram.getPrice() <= budget)
            .max(Comparator.comparing(ram -> ram.getSizeGb() * ram.getSpeedMhz()))
            .orElse(ramRepository.findAll().stream()
                .min(Comparator.comparing(Ram::getPrice))
                .orElse(null));
    }

    private Storage findBestStorage(Double budget) {
        return storageRepository.findAll().stream()
            .filter(storage -> storage.getPrice() <= budget)
            .max(Comparator.comparing(storage ->
                storage.getCapacityGb() * (storage.getType().equalsIgnoreCase("SSD") ? 2 : 1)))
            .orElse(storageRepository.findAll().stream()
                .min(Comparator.comparing(Storage::getPrice))
                .orElse(null));
    }

    private Psu findSuitablePsu(Gpu gpu, Cpu cpu, Double budget) {
        int requiredWattage = 400; // Base requirement
        if (gpu != null) requiredWattage += gpu.getTdp();
        if (cpu != null) requiredWattage += cpu.getTdp();
        requiredWattage = (int) (requiredWattage * 1.2); // 20% headroom

        final int finalWattage = requiredWattage;
        return psuRepository.findAll().stream()
            .filter(psu -> psu.getPrice() <= budget)
            .filter(psu -> psu.getWattage() >= finalWattage)
            .min(Comparator.comparing(Psu::getPrice))
            .orElse(psuRepository.findAll().stream()
                .min(Comparator.comparing(Psu::getPrice))
                .orElse(null));
    }

    private Case findBestCase(Double budget) {
        List<Case> allCases = caseRepository.findAll();
        System.out.println("DEBUG: Total cases found: " + allCases.size());
        System.out.println("DEBUG: Case budget allocation: $" + budget);

        if (!allCases.isEmpty()) {
            Case cheapestCase = allCases.stream()
                .min(Comparator.comparing(Case::getPrice))
                .orElse(null);
            System.out.println("DEBUG: Cheapest case price: $" + cheapestCase.getPrice());
        }

        return caseRepository.findAll().stream()
            .filter(case_ -> case_.getPrice() <= budget)
            .min(Comparator.comparing(Case::getPrice))
            .orElse(caseRepository.findAll().stream()
                .min(Comparator.comparing(Case::getPrice))
                .orElse(null));
    }

    private List<String> generateBuildNotes(String purpose, Gpu gpu, Cpu cpu, String preferredBrand) {
        List<String> notes = new ArrayList<>();

        if ("Gaming".equalsIgnoreCase(purpose)) {
            notes.add("This build is optimized for gaming performance");
            if (gpu != null && gpu.getVram() >= 8) {
                notes.add("GPU has sufficient VRAM for modern games at high settings");
            }
        }

        if (preferredBrand != null && gpu != null) {
            if (preferredBrand.equalsIgnoreCase("NVIDIA") && gpu.getBrand().equalsIgnoreCase("NVIDIA")) {
                notes.add("Build includes NVIDIA GPU as requested for features like DLSS and ray tracing");
            } else if (preferredBrand.equalsIgnoreCase("AMD") && gpu.getBrand().equalsIgnoreCase("AMD")) {
                notes.add("Build includes AMD GPU as requested for excellent price-to-performance ratio");
            }
        }

        if (cpu != null && cpu.getCores() >= 6) {
            notes.add("CPU has sufficient cores for modern gaming and multitasking");
        }

        return notes;
    }

    private static class BudgetAllocation {
        double gpuBudget;
        double cpuBudget;
        double motherboardBudget;
        double ramBudget;
        double storageBudget;
        double psuBudget;
        double caseBudget;
    }
}
