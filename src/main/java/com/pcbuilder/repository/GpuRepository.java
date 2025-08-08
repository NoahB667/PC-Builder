package com.pcbuilder.repository;

import com.pcbuilder.entity.Gpu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GpuRepository extends JpaRepository<Gpu, Long> {

    List<Gpu> findByBrand(String brand);

    @Query("SELECT g FROM Gpu g WHERE g.price BETWEEN :minPrice AND :maxPrice")
    List<Gpu> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    @Query("SELECT g FROM Gpu g WHERE g.vram >= :minVram")
    List<Gpu> findByVramGreaterThanEqual(@Param("minVram") Integer minVram);

    @Query("SELECT g FROM Gpu g WHERE g.tdp <= :maxTdp")
    List<Gpu> findByTdpLessThanEqual(@Param("maxTdp") Integer maxTdp);

    List<Gpu> findByBrandAndPriceLessThanEqualOrderByPriceDesc(String brand, Double maxPrice);
}
