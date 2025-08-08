package com.pcbuilder.repository;

import com.pcbuilder.entity.Cpu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpuRepository extends JpaRepository<Cpu, Long> {

    List<Cpu> findByBrand(String brand);

    List<Cpu> findBySocket(String socket);

    @Query("SELECT c FROM Cpu c WHERE c.price BETWEEN :minPrice AND :maxPrice")
    List<Cpu> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    @Query("SELECT c FROM Cpu c WHERE c.cores >= :minCores")
    List<Cpu> findByCoresGreaterThanEqual(@Param("minCores") Integer minCores);

    List<Cpu> findBySocketAndPriceLessThanEqualOrderByPriceDesc(String socket, Double maxPrice);
}
