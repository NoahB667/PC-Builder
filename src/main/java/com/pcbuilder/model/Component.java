package com.pcbuilder.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

// Represents a single PC component (CPU, GPU, etc.)
@Data
@Entity
@Table(name = "components")
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the component
    private String type; // Type of component (CPU, GPU, etc.)
    private String brand; // Brand of the component
    private String name; // Model name
    private String socket; // Socket type (for CPU/Motherboard compatibility)
    private int powerWatt; // Power consumption in watts
    private double price; // Price in USD
    private int performanceScore; // Performance score (arbitrary scale)
} 