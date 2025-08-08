package com.pcbuilder.repository;

import com.pcbuilder.entity.Psu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PsuRepository extends JpaRepository<Psu, Long> {

    List<Psu> findByBrand(String brand);

    @Query("SELECT p FROM Psu p WHERE p.wattage >= :minWattage")
    List<Psu> findByWattageGreaterThanEqual(@Param("minWattage") Integer minWattage);

    List<Psu> findByCertification(String certification);

    List<Psu> findByModular(String modular);

    @Query("SELECT p FROM Psu p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Psu> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    List<Psu> findByWattageGreaterThanEqualAndModularAndPriceLessThanEqualOrderByPriceDesc(Integer minWattage, String modular, Double maxPrice);
}
