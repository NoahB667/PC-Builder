package com.pcbuilder.repository;

import com.pcbuilder.entity.Build;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildRepository extends JpaRepository<Build, Long> {

    List<Build> findByUserId(Long userId);

    @Query("SELECT b FROM Build b WHERE b.name LIKE %:name%")
    List<Build> findByNameContaining(@Param("name") String name);

    @Query("SELECT b FROM Build b WHERE b.budget BETWEEN :minBudget AND :maxBudget")
    List<Build> findByBudgetRange(@Param("minBudget") Double minBudget, @Param("maxBudget") Double maxBudget);
}
