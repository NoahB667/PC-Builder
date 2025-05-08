package com.pcbuilder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pcbuilder.model.BuildSuggestion;
import com.pcbuilder.model.Component;
import com.pcbuilder.service.ComponentService;

@RestController
@RequestMapping("/api/components") // Base URL for component-related endpoints
@CrossOrigin(origins = "http://localhost:3000") // Allow CORS for frontend development
public class ComponentController {

    private final ComponentService componentService;

    @Autowired
    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @GetMapping // Get all components (To be removed eventually)
    public ResponseEntity<List<Component>> getAllComponents() {
        return ResponseEntity.ok(componentService.getAllComponents());
    }

    @GetMapping("/{type}") // Get components by type (e.g., CPU, GPU)
    public ResponseEntity<List<Component>> getComponentsByType(@PathVariable String type) {
        List<Component> components = componentService.getComponentsByType(type);
        if (components.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if no components are found
        }
        return ResponseEntity.ok(components);
    }

    @GetMapping("/build/suggest") // Suggest a build based on purpose and budget
    public ResponseEntity<BuildSuggestion> suggestBuild(@RequestParam String purpose, @RequestParam double budget) {
        BuildSuggestion suggestion = componentService.suggestBuild(purpose, budget);
        return ResponseEntity.ok(suggestion);
    }
} 