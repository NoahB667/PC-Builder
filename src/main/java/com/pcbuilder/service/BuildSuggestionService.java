package com.pcbuilder.service;

import com.pcbuilder.entity.*;
import com.pcbuilder.repository.*;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
public class BuildSuggestionService {

    private final CpuRepository cpuRepository;

    private final GpuRepository gpuRepository;

    private final MotherboardRepository motherboardRepository;

    private final RamRepository ramRepository;

    private final StorageRepository storageRepository;

    private final PsuRepository psuRepository;

    private final CaseRepository caseRepository;

    private static final int BEAM_WIDTH = 50;

    public BuildSuggestionService(CpuRepository cpuRepository, GpuRepository gpuRepository, MotherboardRepository motherboardRepository, RamRepository ramRepository, StorageRepository storageRepository, PsuRepository psuRepository, CaseRepository caseRepository) {
        this.cpuRepository = cpuRepository;
        this.gpuRepository = gpuRepository;
        this.motherboardRepository = motherboardRepository;
        this.ramRepository = ramRepository;
        this.storageRepository = storageRepository;
        this.psuRepository = psuRepository;
        this.caseRepository = caseRepository;
    }

    public Map<String, Object> suggestBuild(String purpose, Double budget) {
        // Fetch Components (Data Access Layer)
        List<Cpu> cpus = cpuRepository.findAll();
        List<Gpu> gpus = gpuRepository.findAll();
        List<Motherboard> motherboards = motherboardRepository.findAll();
        List<Ram> rams = ramRepository.findAll();
        List<Storage> storages = storageRepository.findAll();
        List<Psu> psus = psuRepository.findAll();
        List<Case> cases = caseRepository.findAll();

        BuildCandidate bestCandidate = executeBeamSearch(purpose, budget, cpus, motherboards, rams, gpus, storages, psus, cases);
        return constructResponse(bestCandidate, purpose, budget);
    }

    private BuildCandidate executeBeamSearch(String purpose, Double budget,
                                             List<Cpu> cpus, List<Motherboard> motherboards,
                                             List<Ram> rams, List<Gpu> gpus,
                                             List<Storage> storages, List<Psu> psus,
                                             List<Case> cases) {

        List<BuildCandidate> candidates = new ArrayList<>();
        candidates.add(new BuildCandidate());

        if ("Gaming".equalsIgnoreCase(purpose)) {
            candidates = expandWithCpus(candidates, cpus, budget, purpose);
            System.out.println("Step CPU: " + candidates.size() + " candidates");
        } else {
            // Only add cpus with integrated graphics
            cpus = cpus.stream()
                    .filter(cpu -> cpu.getGraphics() != null && !cpu.getGraphics().isBlank())
                    .collect(Collectors.toList());
            candidates = expandWithCpus(candidates, cpus, budget, purpose);
        }

        candidates = expandWithMotherboards(candidates, motherboards, budget, purpose);
        System.out.println("Step Motherboard: " + candidates.size() + " candidates");
        if ("Gaming".equalsIgnoreCase(purpose)) {
            candidates = expandWithGpus(candidates, gpus, budget, purpose);
            System.out.println("Step GPU: " + candidates.size() + " candidates");
        }

        candidates = expandWithRams(candidates, rams, budget, purpose);
        System.out.println("Step RAM: " + candidates.size() + " candidates");
        candidates = expandWithStorages(candidates, storages, budget, purpose);
        System.out.println("Step STORAGE: " + candidates.size() + " candidates");
        candidates = expandWithPsus(candidates, psus, budget, purpose);
        System.out.println("Step PSU: " + candidates.size() + " candidates");
        candidates = expandWithCases(candidates, cases, budget, purpose);
        System.out.println("Step CASES: " + candidates.size() + " candidates");
        return candidates.isEmpty() ? null : candidates.get(0);
    }

    // Helper methods for expanding candidates with specific components
    private List<BuildCandidate> expandWithCpus(List<BuildCandidate> candidates, List<Cpu> components, Double budget, String purpose) {
        return expandCandidates(candidates, components, budget, purpose, (cand, comp) -> {
            cand.cpu = comp;
            cand.totalCost += comp.getPrice();
            return cand;
        });
    }

    private List<BuildCandidate> expandWithGpus(List<BuildCandidate> candidates, List<Gpu> components, Double budget, String purpose) {
        return expandCandidates(candidates, components, budget, purpose, (cand, comp) -> {
            cand.gpu = comp;
            cand.totalCost += comp.getPrice();
            return cand;
        });
    }

    private List<BuildCandidate> expandWithMotherboards(List<BuildCandidate> candidates, List<Motherboard> components, Double budget, String purpose) {
        return expandCandidates(candidates, components, budget, purpose, (cand, comp) -> {
            if (cand.cpu != null && comp.getSocket().equals(cand.cpu.getSocket())) {
                cand.motherboard = comp;
                cand.totalCost += comp.getPrice();
                return cand;
            }
            return null;
        });
    }

    private List<BuildCandidate> expandWithRams(List<BuildCandidate> candidates, List<Ram> components, Double budget, String purpose) {
        return expandCandidates(candidates, components, budget, purpose, (cand, comp) -> {
            if (cand.motherboard != null && comp.getGeneration().equalsIgnoreCase(cand.motherboard.getRamGen())) {
                cand.ram = comp;
                cand.totalCost += comp.getPrice();
                return cand;
            }
            return null;
        });
    }

    private List<BuildCandidate> expandWithStorages(List<BuildCandidate> candidates, List<Storage> components, Double budget, String purpose) {
        return expandCandidates(candidates, components, budget, purpose, (cand, comp) -> {
            cand.storage = comp;
            cand.totalCost += comp.getPrice();
            return cand;
        });
    }

    private List<BuildCandidate> expandWithPsus(List<BuildCandidate> candidates, List<Psu> components, Double budget, String purpose) {
        return expandCandidates(candidates, components, budget, purpose, (cand, comp) -> {
            int requiredWattage = calculateRequiredWattage(cand);
            if (comp.getWattage() >= requiredWattage) {
                cand.psu = comp;
                cand.totalCost += comp.getPrice();
                return cand;
            }
            return null;
        });
    }

    private List<BuildCandidate> expandWithCases(List<BuildCandidate> candidates, List<Case> components, Double budget, String purpose) {
        return expandCandidates(candidates, components, budget, purpose, (cand, comp) -> {
            cand.pcCase = comp;
            cand.totalCost += comp.getPrice();
            return cand;
        });
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

                    if (result != null) {
                        double futureBuffer = calculateFutureBuffer(result);
                        if (result.totalCost + futureBuffer <= budget) {
                            result.score = calculateScore(result, purpose);
                            nextGeneration.add(result);
                        }
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
        boolean isGaming = purpose.equalsIgnoreCase("Gaming");

        // 1. Initial Path-Specific Weights
        double cpuWeight = isGaming ? 0.35 : 0.70;
        double gpuWeight = isGaming ? 0.55 : 0.00;
        double ramWeight = isGaming ? 0.05 : 0.20;
        double storageWeight = 0.05;
        double totalScore = 0;

        if (build.cpu != null) {
            totalScore += build.cpu.getBenchmarkScore() * cpuWeight;
        }

        if (isGaming && build.gpu != null) {
            totalScore += build.gpu.getBenchmarkScore() * gpuWeight;
        }

        if (build.ram != null) {
            double rScore = build.ram.getBenchmarkScore();
            if (build.ram.getNumModules() < 2) rScore *= 0.8;
            totalScore += rScore * ramWeight;
        }

        if (build.storage != null) {
            totalScore += build.storage.getBenchmarkScore() * storageWeight;
        }

        // Bottleneck Penalty (Gaming Only)
        if (isGaming && build.cpu != null && build.gpu != null) {
            double diff = Math.abs(build.cpu.getBenchmarkScore() - build.gpu.getBenchmarkScore());
            if (diff > 40) totalScore *= 0.85;
        }

        return totalScore;
    }

    private double calculateFutureBuffer(BuildCandidate build) {
        double buffer = 0;
        if (build.storage == null) buffer += 80;
        if (build.psu == null) buffer += 100;
        if (build.pcCase == null) buffer += 70;
        if (build.ram == null) buffer += 100;
        return buffer;
    }
    private int calculateRequiredWattage(BuildCandidate build) {
        int wattage = 100;
        if (build.cpu != null) wattage += build.cpu.getTdp();
        if (build.gpu != null) wattage += build.gpu.getTdp();
        return (int) (wattage * 1.15);
    }

    private Map<String, Object> constructResponse(BuildCandidate build, String purpose, Double budget) {
        Map<String, Object> map = new HashMap<>();
        map.put("purpose", purpose);
        map.put("budget", budget);

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
        @Getter
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

