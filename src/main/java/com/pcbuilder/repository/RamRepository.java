package com.pcbuilder.repository;

import com.pcbuilder.entity.Ram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RamRepository extends JpaRepository<Ram, Long> {

    List<Ram> findByBrand(String brand);

    List<Ram> findByType(String type);

    @Query("SELECT r FROM Ram r WHERE r.price BETWEEN :minPrice AND :maxPrice")
    List<Ram> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    @Query("SELECT r FROM Ram r WHERE r.sizeGb >= :minSize")
    List<Ram> findBySizeGbGreaterThanEqual(@Param("minSize") Integer minSize);

    @Query("SELECT r FROM Ram r WHERE r.speedMhz >= :minSpeed")
    List<Ram> findBySpeedMhzGreaterThanEqual(@Param("minSpeed") Integer minSpeed);

    List<Ram> findByTypeAndSizeGbAndPriceLessThanEqualOrderByPriceDesc(String type, Integer sizeGb, Double maxPrice);
}
