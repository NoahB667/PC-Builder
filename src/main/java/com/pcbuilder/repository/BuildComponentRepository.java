package com.pcbuilder.repository;

import com.pcbuilder.entity.BuildComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildComponentRepository extends JpaRepository<BuildComponent, Long> {

    List<BuildComponent> findByBuildId(Long buildId);

    List<BuildComponent> findByComponentType(String componentType);

    @Query("SELECT bc FROM BuildComponent bc WHERE bc.buildId = :buildId AND bc.componentType = :componentType")
    List<BuildComponent> findByBuildIdAndComponentType(@Param("buildId") Long buildId, @Param("componentType") String componentType);

    void deleteByBuildId(Long buildId);
}
