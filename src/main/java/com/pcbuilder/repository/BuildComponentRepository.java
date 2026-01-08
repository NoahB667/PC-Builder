package com.pcbuilder.repository;

import com.pcbuilder.entity.BuildComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildComponentRepository extends JpaRepository<BuildComponent, Long> {

    void deleteByBuildId(Long buildId);
}
