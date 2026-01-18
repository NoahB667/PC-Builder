package com.pcbuilder.controller;

import com.pcbuilder.entity.*;
import com.pcbuilder.repository.*;
import com.pcbuilder.service.BuildSuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/components")
public class ComponentController {

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

    @Autowired
    private BuildSuggestionService buildSuggestionService;

    @GetMapping
    public ResponseEntity<String> getAllComponents() {
        return ResponseEntity.ok("Components API is working!");
    }

    @GetMapping("/cpu")
    public ResponseEntity<List<Cpu>> getAllCpus() {
        return ResponseEntity.ok(cpuRepository.findAll());
    }

    @GetMapping("/gpu")
    public ResponseEntity<List<Gpu>> getAllGpus() {
        return ResponseEntity.ok(gpuRepository.findAll());
    }

    @GetMapping("/motherboard")
    public ResponseEntity<List<Motherboard>> getAllMotherboards() {
        return ResponseEntity.ok(motherboardRepository.findAll());
    }

    @GetMapping("/ram")
    public ResponseEntity<List<Ram>> getAllRam() {
        return ResponseEntity.ok(ramRepository.findAll());
    }

    @GetMapping("/storage")
    public ResponseEntity<List<Storage>> getAllStorage() {
        return ResponseEntity.ok(storageRepository.findAll());
    }

    @GetMapping("/psu")
    public ResponseEntity<List<Psu>> getAllPsus() {
        return ResponseEntity.ok(psuRepository.findAll());
    }

    @GetMapping("/case")
    public ResponseEntity<List<Case>> getAllCases() {
        return ResponseEntity.ok(caseRepository.findAll());
    }

    @GetMapping("/build/suggest")
    public ResponseEntity<Map<String, Object>> suggestBuild(
            @RequestParam String purpose,
            @RequestParam Double budget) {

        Map<String, Object> suggestion = buildSuggestionService.suggestBuild(purpose, budget);
        return ResponseEntity.ok(suggestion);
    }
}
