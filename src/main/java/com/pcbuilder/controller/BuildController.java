package com.pcbuilder.controller;

import com.pcbuilder.dto.BuildDTO;
import com.pcbuilder.service.BuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/builds")
public class BuildController {

    @Autowired
    private BuildService buildService;

    @GetMapping
    public ResponseEntity<List<BuildDTO>> getAllBuilds() {
        return ResponseEntity.ok(buildService.getAllBuilds());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuildDTO> getBuildById(@PathVariable Long id) {
        return buildService.getBuildById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BuildDTO>> getBuildsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(buildService.getBuildsByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<BuildDTO> createBuild(@RequestBody BuildDTO buildDTO) {
        BuildDTO createdBuild = buildService.createBuild(buildDTO);
        return ResponseEntity.ok(createdBuild);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuildDTO> updateBuild(@PathVariable Long id, @RequestBody BuildDTO buildDTO) {
        BuildDTO updatedBuild = buildService.updateBuild(id, buildDTO);
        return ResponseEntity.ok(updatedBuild);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuild(@PathVariable Long id) {
        buildService.deleteBuild(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<BuildDTO>> searchBuilds(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minBudget,
            @RequestParam(required = false) Double maxBudget) {

        if (name != null) {
            return ResponseEntity.ok(buildService.searchBuildsByName(name));
        }
        if (minBudget != null && maxBudget != null) {
            return ResponseEntity.ok(buildService.getBuildsByBudgetRange(minBudget, maxBudget));
        }
        return ResponseEntity.ok(buildService.getAllBuilds());
    }
}
