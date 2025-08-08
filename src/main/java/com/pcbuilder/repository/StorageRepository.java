package com.pcbuilder.repository;

import com.pcbuilder.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {

    List<Storage> findByBrand(String brand);

    List<Storage> findByType(String type);

    @Query("SELECT s FROM Storage s WHERE s.price BETWEEN :minPrice AND :maxPrice")
    List<Storage> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    @Query("SELECT s FROM Storage s WHERE s.capacityGb >= :minCapacity")
    List<Storage> findByCapacityGbGreaterThanEqual(@Param("minCapacity") Integer minCapacity);

    @Query("SELECT s FROM Storage s WHERE s.speedMbps >= :minSpeed")
    List<Storage> findBySpeedMbpsGreaterThanEqual(@Param("minSpeed") Integer minSpeed);

    List<Storage> findByTypeAndCapacityGbGreaterThanEqualAndPriceLessThanEqualOrderByPriceDesc(String type, Integer minCapacity, Double maxPrice);
}
