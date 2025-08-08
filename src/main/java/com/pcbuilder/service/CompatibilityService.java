package com.pcbuilder.service;

import com.pcbuilder.entity.*;
import com.pcbuilder.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompatibilityService {

    @Autowired
    private CpuRepository cpuRepository;

    @Autowired
    private MotherboardRepository motherboardRepository;

    @Autowired
    private RamRepository ramRepository;

    @Autowired
    private GpuRepository gpuRepository;

    @Autowired
    private PsuRepository psuRepository;

    @Autowired
    private CaseRepository caseRepository;

    public boolean isSocketCompatible(Long cpuId, Long motherboardId) {
        Cpu cpu = cpuRepository.findById(cpuId).orElse(null);
        Motherboard motherboard = motherboardRepository.findById(motherboardId).orElse(null);

        if (cpu == null || motherboard == null) {
            return false;
        }

        return cpu.getSocket() != null && cpu.getSocket().equals(motherboard.getSocket());
    }

    public boolean isRamCompatible(Long ramId, Long motherboardId) {
        Ram ram = ramRepository.findById(ramId).orElse(null);
        Motherboard motherboard = motherboardRepository.findById(motherboardId).orElse(null);

        if (ram == null || motherboard == null) {
            return false;
        }

        return ram.getType() != null && ram.getType().equals(motherboard.getRamType());
    }

    public boolean isPowerSupplyAdequate(Long psuId, List<Long> componentIds) {
        Psu psu = psuRepository.findById(psuId).orElse(null);
        if (psu == null) {
            return false;
        }

        int totalTdp = calculateTotalTdp(componentIds);
        return psu.getWattage() != null && psu.getWattage() >= (totalTdp * 1.2); // 20% headroom
    }

    public boolean isFormFactorCompatible(Long motherboardId, Long caseId) {
        Motherboard motherboard = motherboardRepository.findById(motherboardId).orElse(null);
        Case pcCase = caseRepository.findById(caseId).orElse(null);

        if (motherboard == null || pcCase == null) {
            return false;
        }

        return motherboard.getFormFactor() != null &&
               pcCase.getFormFactor() != null &&
               isFormFactorFit(motherboard.getFormFactor(), pcCase.getFormFactor());
    }

    public boolean validateCompleteBuild(Long cpuId, Long motherboardId, Long ramId,
                                       Long gpuId, Long psuId, Long caseId) {
        return isSocketCompatible(cpuId, motherboardId) &&
               isRamCompatible(ramId, motherboardId) &&
               isPowerSupplyAdequate(psuId, List.of(cpuId, gpuId)) &&
               isFormFactorCompatible(motherboardId, caseId);
    }

    private int calculateTotalTdp(List<Long> componentIds) {
        int totalTdp = 0;

        for (Long componentId : componentIds) {
            // Check if it's a CPU
            Cpu cpu = cpuRepository.findById(componentId).orElse(null);
            if (cpu != null && cpu.getTdp() != null) {
                totalTdp += cpu.getTdp();
                continue;
            }

            // Check if it's a GPU
            Gpu gpu = gpuRepository.findById(componentId).orElse(null);
            if (gpu != null && gpu.getTdp() != null) {
                totalTdp += gpu.getTdp();
            }
        }

        return totalTdp;
    }

    private boolean isFormFactorFit(String motherboardFormFactor, String caseFormFactor) {
        // Basic form factor compatibility logic
        if ("ATX".equals(caseFormFactor)) {
            return List.of("ATX", "Micro-ATX", "Mini-ITX").contains(motherboardFormFactor);
        } else if ("Micro-ATX".equals(caseFormFactor)) {
            return List.of("Micro-ATX", "Mini-ITX").contains(motherboardFormFactor);
        } else if ("Mini-ITX".equals(caseFormFactor)) {
            return "Mini-ITX".equals(motherboardFormFactor);
        }

        return false;
    }
}
