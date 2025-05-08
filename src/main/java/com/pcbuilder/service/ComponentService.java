package com.pcbuilder.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.pcbuilder.model.BuildSuggestion;
import com.pcbuilder.model.Component;

@Service
public class ComponentService {
    private final List<Component> components;
    private boolean isGaming;

    // Constructor loads components from CSV at startup
    public ComponentService() {
        this.components = loadComponents();
    }

    // Loads components from the CSV file in resources
    private List<Component> loadComponents() {
        List<Component> loadedComponents = new ArrayList<>();
        try {
            ClassPathResource resource = new ClassPathResource("components.csv");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                // Skip header
                reader.readLine();
                
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(",");
                    if (values.length >= 8) {
                        Component component = new Component();
                        component.setId(Long.parseLong(values[0].trim()));
                        component.setType(values[1].trim());
                        component.setBrand(values[2].trim());
                        component.setName(values[3].trim());
                        component.setSocket(values[4].trim());
                        component.setPowerWatt(Integer.parseInt(values[5].trim()));
                        component.setPrice(Double.parseDouble(values[6].trim()));
                        component.setPerformanceScore(Integer.parseInt(values[7].trim()));
                        loadedComponents.add(component);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadedComponents;
    }

    // Returns all loaded components
    public List<Component> getAllComponents() {
        return components;
    }

    // Returns all components of a given type
    public List<Component> getComponentsByType(String type) {
        return components.stream()
                .filter(c -> c.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    // Main method to suggest a build based on purpose and budget
    public BuildSuggestion suggestBuild(String purpose, double budget) {
        List<Component> selectedComponents = new ArrayList<>();
        isGaming = "Gaming".equalsIgnoreCase(purpose);
        
        // Start with the most budget-friendly components
        Component cpu = selectBudgetCPU();
        if (cpu == null) {
            return new BuildSuggestion(Collections.emptyList(), false, 0);
        }
        selectedComponents.add(cpu);
        double currentTotal = cpu.getPrice();

        // For gaming builds, add GPU
        if (isGaming) {
            Component gpu = selectBudgetGPU();
            if (gpu == null) {
                return new BuildSuggestion(Collections.emptyList(), false, 0);
            }
            selectedComponents.add(gpu);
            currentTotal += gpu.getPrice();
        }

        // Add remaining essential components
        Component motherboard = selectBudgetMotherboard(cpu.getSocket());
        if (motherboard == null) {
            return new BuildSuggestion(Collections.emptyList(), false, 0);
        }
        selectedComponents.add(motherboard);
        currentTotal += motherboard.getPrice();

        Component ram = selectBudgetRAM();
        if (ram == null) {
            return new BuildSuggestion(Collections.emptyList(), false, 0);
        }
        selectedComponents.add(ram);
        currentTotal += ram.getPrice();

        Component storage = selectBudgetStorage();
        if (storage == null) {
            return new BuildSuggestion(Collections.emptyList(), false, 0);
        }
        selectedComponents.add(storage);
        currentTotal += storage.getPrice();

        // Calculate total power consumption for PSU selection
        int totalPower = calculateTotalPower(selectedComponents);
        Component psu = selectBudgetPSU(totalPower);
        if (psu == null) {
            return new BuildSuggestion(Collections.emptyList(), false, 0);
        }
        selectedComponents.add(psu);
        currentTotal += psu.getPrice();

        // Upgrade components one by one until hitting the budget
        while (currentTotal < budget) {
            double previousTotal = currentTotal;
            Component upgradedComponent = findBestUpgrade(selectedComponents, budget - currentTotal);
            
            if (upgradedComponent == null) {
                break; // No more possible upgrades
            }

            // Replace the old component with the upgraded one
            selectedComponents.removeIf(c -> c.getType().equals(upgradedComponent.getType()));
            selectedComponents.add(upgradedComponent);
            currentTotal = selectedComponents.stream().mapToDouble(Component::getPrice).sum();

            // If we went over budget, revert the last upgrade
            if (currentTotal > budget) {
                selectedComponents.removeIf(c -> c.getType().equals(upgradedComponent.getType()));
                selectedComponents.add(findComponentByType(upgradedComponent.getType(), previousTotal - currentTotal + upgradedComponent.getPrice()));
                currentTotal = previousTotal;
                break;
            }
        }

        // Add case if there's any budget left
        Component pccase = selectCase((int)(budget - currentTotal));
        if (pccase != null) {
            selectedComponents.add(pccase);
            currentTotal += pccase.getPrice();
        }

        // Check compatibility and return the build suggestion
        boolean isCompatible = checkCompatibility(selectedComponents);
        return new BuildSuggestion(selectedComponents, isCompatible, currentTotal);
    }

    // Selects the cheapest CPU
    private Component selectBudgetCPU() {
        return components.stream()
                .filter(c -> "CPU".equalsIgnoreCase(c.getType()))
                .min(Comparator.comparingDouble(Component::getPrice))
                .orElse(null);
    }

    // Selects the cheapest GPU
    private Component selectBudgetGPU() {
        return components.stream()
                .filter(c -> "GPU".equalsIgnoreCase(c.getType()))
                .min(Comparator.comparingDouble(Component::getPrice))
                .orElse(null);
    }

    // Selects the cheapest compatible motherboard
    private Component selectBudgetMotherboard(String socket) {
        return components.stream()
                .filter(c -> "Motherboard".equalsIgnoreCase(c.getType()))
                .filter(c -> c.getSocket().equals(socket))
                .min(Comparator.comparingDouble(Component::getPrice))
                .orElse(null);
    }

    // Selects the cheapest RAM
    private Component selectBudgetRAM() {
        return components.stream()
                .filter(c -> "RAM".equalsIgnoreCase(c.getType()))
                .min(Comparator.comparingDouble(Component::getPrice))
                .orElse(null);
    }

    // Selects the cheapest storage
    private Component selectBudgetStorage() {
        return components.stream()
                .filter(c -> "Storage".equalsIgnoreCase(c.getType()))
                .min(Comparator.comparingDouble(Component::getPrice))
                .orElse(null);
    }

    // Selects the cheapest PSU that meets the power requirement
    private Component selectBudgetPSU(int requiredPower) {
        return components.stream()
                .filter(c -> "PSU".equalsIgnoreCase(c.getType()))
                .filter(c -> c.getPowerWatt() >= requiredPower)
                .min(Comparator.comparingDouble(Component::getPrice))
                .orElse(null);
    }

    // Finds the best upgrade for any component within the available budget
    private Component findBestUpgrade(List<Component> currentComponents, double availableBudget) {
        Component bestUpgrade = null;
        double bestValue = 0;

        for (Component current : currentComponents) {
            if ("Case".equalsIgnoreCase(current.getType())) {
                continue; // Skip case upgrades
            }

            Component upgrade = components.stream()
                    .filter(c -> c.getType().equals(current.getType()))
                    .filter(c -> c.getPrice() > current.getPrice())
                    .filter(c -> c.getPrice() <= current.getPrice() + availableBudget)
                    .max(Comparator.comparingDouble(c -> (c.getPerformanceScore() - current.getPerformanceScore()) / (c.getPrice() - current.getPrice())))
                    .orElse(null);

            if (upgrade != null) {
                double value = (upgrade.getPerformanceScore() - current.getPerformanceScore()) / (upgrade.getPrice() - current.getPrice());
                if (value > bestValue) {
                    bestValue = value;
                    bestUpgrade = upgrade;
                }
            }
        }

        return bestUpgrade;
    }

    // Finds the best component of a given type within a price
    private Component findComponentByType(String type, double price) {
        return components.stream()
                .filter(c -> c.getType().equals(type))
                .filter(c -> c.getPrice() <= price)
                .max(Comparator.comparingDouble(Component::getPerformanceScore))
                .orElse(null);
    }

    // Selects the cheapest case
    private Component selectCase(int budget) {
        return components.stream()
                .filter(c -> "Case".equalsIgnoreCase(c.getType()))
                .filter(c -> c.getPrice() <= budget)
                .min(Comparator.comparingDouble(Component::getPrice))  // Select the cheapest case that fits the budget
                .orElse(null);
    }

    // Calculates the total power consumption of selected components
    private int calculateTotalPower(List<Component> components) {
        return components.stream()
                .mapToInt(Component::getPowerWatt)
                .sum();
    }

    // Checks if the selected components form a compatible build
    private boolean checkCompatibility(List<Component> components) {
        // Basic compatibility check - ensure all required components are present
        boolean hasCPU = components.stream().anyMatch(c -> "CPU".equalsIgnoreCase(c.getType()));
        boolean hasMotherboard = components.stream().anyMatch(c -> "Motherboard".equalsIgnoreCase(c.getType()));
        boolean hasRAM = components.stream().anyMatch(c -> "RAM".equalsIgnoreCase(c.getType()));
        boolean hasStorage = components.stream().anyMatch(c -> "Storage".equalsIgnoreCase(c.getType()));
        boolean hasPSU = components.stream().anyMatch(c -> "PSU".equalsIgnoreCase(c.getType()));
        
        // For gaming builds, also check for GPU
        boolean hasGPU = components.stream().anyMatch(c -> "GPU".equalsIgnoreCase(c.getType()));
        
        return hasCPU && hasMotherboard && hasRAM && hasStorage && hasPSU && (!isGaming || hasGPU);
    }
} 