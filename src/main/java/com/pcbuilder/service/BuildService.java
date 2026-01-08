package com.pcbuilder.service;

import com.pcbuilder.dto.BuildDTO;
import com.pcbuilder.entity.Build;
import com.pcbuilder.repository.BuildRepository;
import com.pcbuilder.repository.BuildComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BuildService {

    @Autowired
    private BuildRepository buildRepository;

    @Autowired
    private BuildComponentRepository buildComponentRepository;

    public List<BuildDTO> getAllBuilds() {
        return buildRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<BuildDTO> getBuildById(Long id) {
        return buildRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<BuildDTO> getBuildsByUserId(Long userId) {
        return buildRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BuildDTO createBuild(BuildDTO buildDTO) {
        Build build = convertToEntity(buildDTO);
        Build savedBuild = buildRepository.save(build);
        return convertToDTO(savedBuild);
    }

    public BuildDTO updateBuild(Long id, BuildDTO buildDTO) {
        Build build = buildRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Build not found with id: " + id));

        build.setName(buildDTO.getName());
        build.setBudget(buildDTO.getBudget());

        Build updatedBuild = buildRepository.save(build);
        return convertToDTO(updatedBuild);
    }

    public void deleteBuild(Long id) {
        buildComponentRepository.deleteByBuildId(id);
        buildRepository.deleteById(id);
    }

    public List<BuildDTO> searchBuildsByName(String name) {
        return buildRepository.findByNameContaining(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BuildDTO> getBuildsByBudgetRange(Double minBudget, Double maxBudget) {
        return buildRepository.findByBudgetRange(minBudget, maxBudget).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private BuildDTO convertToDTO(Build build) {
        return new BuildDTO(
                build.getId(),
                build.getUserId(),
                build.getName(),
                build.getBudget(),
                build.getCreatedAt()
        );
    }

    private Build convertToEntity(BuildDTO buildDTO) {
        Build build = new Build();
        build.setUserId(buildDTO.getUserId());
        build.setName(buildDTO.getName());
        build.setBudget(buildDTO.getBudget());
        build.setCreatedAt(LocalDateTime.now());
        return build;
    }
}
