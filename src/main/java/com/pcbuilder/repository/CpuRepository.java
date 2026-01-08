package com.pcbuilder.repository;

import com.pcbuilder.entity.Cpu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpuRepository extends JpaRepository<Cpu, Long> {

}
