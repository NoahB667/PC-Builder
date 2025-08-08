package com.pcbuilder.repository;

import com.pcbuilder.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {

    List<Case> findByBrand(String brand);

    List<Case> findByFormFactor(String formFactor);

    @Query("SELECT c FROM Case c WHERE c.price BETWEEN :minPrice AND :maxPrice")
    List<Case> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    List<Case> findByFormFactorAndPriceLessThanEqualOrderByPriceDesc(String formFactor, Double maxPrice);
}
