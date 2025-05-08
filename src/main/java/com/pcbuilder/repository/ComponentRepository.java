package com.pcbuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pcbuilder.model.Component;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> // Repository for CRUD operations on Component entities
{
    // Add custom query methods if needed
} 