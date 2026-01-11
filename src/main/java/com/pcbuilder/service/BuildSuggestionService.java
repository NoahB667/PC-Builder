package com.pcbuilder.service;

import com.pcbuilder.entity.*;
import com.pcbuilder.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiFunction;
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

    private static final int BEAM_WIDTH = 5;

    public Map<String, Object> suggestBuild(String purpose, Double budget, String preferredBrand) {
        // Fetch Components (Data Access Layer)
        List<Cpu> cpus = filterCpus(cpuRepository.findAll(), preferredBrand);
        List<Gpu> gpus = filterGpus(gpuRepository.findAll(), preferredBrand);
        List<Motherboard> motherboards = motherboardRepository.findAll();
        List<Ram> rams = ramRepository.findAll();
        List<Storage> storages = storageRepository.findAll();
        List<Psu> psus = psuRepository.findAll();
        List<Case> cases = caseRepository.findAll();

        BuildCandidate bestCandidate = executeBeamSearch(purpose, budget, cpus, motherboards, rams, gpus, storages, psus, cases);
        return constructResponse(bestCandidate, purpose, budget, preferredBrand);
    }

    private BuildCandidate executeBeamSearch(String purpose, Double budget,
                             List<Cpu> cpus, List<Motherboard> mbs,
                             List<Ram> rams, List<Gpu> gpus,
                             List<Storage> storages, List<Psu> psus,
                             List<Case> cases) {

        List<BuildCandidate> candidates = new ArrayList<>();
        candidates.add(new BuildCandidate());
        if ("Gaming".equalsIgnoreCase(purpose)) {
            // Gaming: Prioritize GPU first
            candidates = expandCandidates(candidates, gpus, budget, purpose, (cand, gpu) -> {
                cand.gpu = gpu;
                cand.totalCost += gpu.getPrice();
                return cand;
            });

            candidates = expandCandidates(candidates, cpus, budget, purpose, (cand, cpu) -> {
                cand.cpu = cpu;
                cand.totalCost += cpu.getPrice();
                return cand;
            });

            candidates = expandCandidates(candidates, mbs, budget, purpose, (cand, mb) -> {
                if (cand.cpu != null && mb.getSocket().equals(cand.cpu.getSocket())) {
                    cand.motherboard = mb;
                    cand.totalCost += mb.getPrice();
                    return cand;
                }
                return null;
            });

            candidates = expandCandidates(candidates, rams, budget, purpose, (cand, ram) -> {
                cand.ram = ram;
                cand.totalCost += ram.getPrice();
                return cand;
            });

            candidates = expandCandidates(candidates, storages, budget, purpose, (cand, storage) -> {
                cand.storage = storage;
                cand.totalCost += storage.getPrice();
                return cand;
            });

            candidates = expandCandidates(candidates, psus, budget, purpose, (cand, psu) -> {
                int requiredWattage = calculateRequiredWattage(cand);
                if (psu.getWattage() >= requiredWattage) {
                    cand.psu = psu;
                    cand.totalCost += psu.getPrice();
                    return cand;
                }
                return null;
            });

            candidates = expandCandidates(candidates, cases, budget, purpose, (cand, pcCase) -> {
                cand.pcCase = pcCase;
                cand.totalCost += pcCase.getPrice();
                return cand;
            });

        } else {
            candidates = expandCandidates(candidates, cpus, budget, purpose, (cand, cpu) -> {
                cand.cpu = cpu;
                cand.totalCost += cpu.getPrice();
                return cand;
            });

            candidates = expandCandidates(candidates, mbs, budget, purpose, (cand, mb) -> {
                if (cand.cpu != null && mb.getSocket().equals(cand.cpu.getSocket())) {
                    cand.motherboard = mb;
                    cand.totalCost += mb.getPrice();
                    return cand;
                }
                return null;
            });

            candidates = expandCandidates(candidates, rams, budget, purpose, (cand, ram) -> {
                cand.ram = ram;
                cand.totalCost += ram.getPrice();
                return cand;
            });

            candidates = expandCandidates(candidates, gpus, budget, purpose, (cand, gpu) -> {
                cand.gpu = gpu;
                cand.totalCost += gpu.getPrice();
                return cand;
            });

            candidates = expandCandidates(candidates, storages, budget, purpose, (cand, storage) -> {
                cand.storage = storage;
                cand.totalCost += storage.getPrice();
                return cand;
            });

            candidates = expandCandidates(candidates, psus, budget, purpose, (cand, psu) -> {
                int requiredWattage = calculateRequiredWattage(cand);
                if (psu.getWattage() >= requiredWattage) {
                    cand.psu = psu;
                    cand.totalCost += psu.getPrice();
                    return cand;
                }
                return null;
            });

            candidates = expandCandidates(candidates, cases, budget, purpose, (cand, pcCase) -> {
                cand.pcCase = pcCase;
                cand.totalCost += pcCase.getPrice();
                return cand;
            });
        }
        return candidates.isEmpty() ? null : candidates.get(0);
    }

    private <T> List<BuildCandidate> expandCandidates(List<BuildCandidate> currentCandidates,
                                                      List<T> components,
                                                      Double budget,
                                                      String purpose,
                                                      BiFunction<BuildCandidate, T, BuildCandidate> transition) {
        List<BuildCandidate> nextGeneration = new ArrayList<>();

        for (BuildCandidate candidate : currentCandidates) {
            for (T component : components) {
                try {
                    // Clone candidate state to branch out
                    BuildCandidate newCand = candidate.clone();

                    // Apply transition (add component)
                    BuildCandidate result = transition.apply(newCand, component);

                    if (result != null && result.totalCost <= budget) {
                        result.score = calculateScore(result, purpose);
                        nextGeneration.add(result);
                    }
                } catch (CloneNotSupportedException e) {
                    System.err.println("Clone failed: " + e.getMessage());
                }
            }
        }

        // Beam Pruning: Sort by score DESC and take top K (BEAM_WIDTH)
        return nextGeneration.stream()
                .sorted((b1, b2) -> Double.compare(b2.score, b1.score))
                .limit(BEAM_WIDTH)
                .collect(Collectors.toList());
    }

    private double calculateScore(BuildCandidate build, String purpose) {
        double score = 0;
        double cpuWeight = purpose.equalsIgnoreCase("Workstation") ? 1.5 : 1.0;
        double gpuWeight = purpose.equalsIgnoreCase("Gaming") ? 1.5 : 1.0;
        double ramWeight = purpose.equalsIgnoreCase("Workstation") ? 1.2 : 0.8;

        if (build.cpu != null) {
            score += (build.cpu.getCores() * build.cpu.getThreads() * build.cpu.getBoostClock()) * cpuWeight;
        }
        if (build.gpu != null) {
            score += (build.gpu.getVram() * 100) * gpuWeight;
        }
        if (build.ram != null) {
            score += (build.ram.getSizeGb() * build.ram.getSpeedMhz() / 100.0) * ramWeight;
        }
        if (build.storage != null) {
            score += build.storage.getCapacityGb() * (build.storage.getType().contains("SSD") ? 2 : 0.5);
        }
        return score;
    }

    private int calculateRequiredWattage(BuildCandidate b) {
        int w = 300; // base system wattage
        if (b.cpu != null) w += b.cpu.getTdp();
        if (b.gpu != null) w += b.gpu.getTdp();
        return (int) (w * 1.2); // 20% overhead
    }

    private List<Cpu> filterCpus(List<Cpu> cpus, String brand) {
        if ("AMD".equalsIgnoreCase(brand)) {
             return cpus.stream().filter(c -> c.getBrand().equalsIgnoreCase("AMD")).collect(Collectors.toList());
        }
        return cpus;
    }

    private List<Gpu> filterGpus(List<Gpu> gpus, String brand) {
        if ("NVIDIA".equalsIgnoreCase(brand)) {
            return gpus.stream().filter(g -> g.getBrand().equalsIgnoreCase("NVIDIA")).collect(Collectors.toList());
        } else if ("AMD".equalsIgnoreCase(brand)) {
            return gpus.stream().filter(g -> g.getBrand().equalsIgnoreCase("AMD")).collect(Collectors.toList());
        }
        return gpus;
    }

    private Map<String, Object> constructResponse(BuildCandidate build, String purpose, Double budget, String brand) {
        Map<String, Object> map = new HashMap<>();
        map.put("purpose", purpose);
        map.put("budget", budget);
        map.put("preferredBrand", brand);

        if (build == null) {
            map.put("error", "No valid build found within budget");
            return map;
        }

        map.put("totalCost", Math.round(build.totalCost * 100.0) / 100.0);
        map.put("score", Math.round(build.score));
        map.put("remainingBudget", Math.round((budget - build.totalCost) * 100.0) / 100.0);

        Map<String, Object> components = new HashMap<>();
        components.put("cpu", build.cpu);
        components.put("motherboard", build.motherboard);
        components.put("ram", build.ram);
        components.put("gpu", build.gpu);
        components.put("storage", build.storage);
        components.put("psu", build.psu);
        components.put("case", build.pcCase);

        map.put("components", components);
        return map;
    }

    private static class BuildCandidate implements Cloneable {
        Cpu cpu;
        Motherboard motherboard;
        Ram ram;
        Gpu gpu;
        Storage storage;
        Psu psu;
        Case pcCase;
        double totalCost = 0;
        double score = 0;

        public BuildCandidate() {
        }

        @Override
        public BuildCandidate clone() throws CloneNotSupportedException {
            return (BuildCandidate) super.clone();
        }
    }
}

