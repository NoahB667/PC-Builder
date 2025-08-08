package com.pcbuilder.repository;

import com.pcbuilder.entity.Motherboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotherboardRepository extends JpaRepository<Motherboard, Long> {

    List<Motherboard> findByBrand(String brand);

    List<Motherboard> findBySocket(String socket);

    List<Motherboard> findByFormFactor(String formFactor);

    List<Motherboard> findByRamType(String ramType);

    @Query("SELECT m FROM Motherboard m WHERE m.price BETWEEN :minPrice AND :maxPrice")
    List<Motherboard> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    @Query("SELECT m FROM Motherboard m WHERE m.ramSlots >= :minSlots")
    List<Motherboard> findByRamSlotsGreaterThanEqual(@Param("minSlots") Integer minSlots);

    List<Motherboard> findBySocketAndFormFactorAndPriceLessThanEqualOrderByPriceDesc(String socket, String formFactor, Double maxPrice);
}
