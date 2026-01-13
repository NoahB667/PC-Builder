package com.pcbuilder.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rams")
public class Ram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model_name", nullable = false)
    private String name;

    @Column(name = "generation", nullable = false)
    private String generation;

    @Column(name = "speed_mhz", nullable = false)
    private int speedMhz;

    @Column(name = "cas_latency", nullable = false)
    private int casLatency;

    @Column(name = "total_capacity_gb", nullable = false)
    private int totalCapacityGb;

    @Column(name = "num_modules", nullable = false)
    private int numModules;

    @Column(name = "is_expo", nullable = false)
    private boolean isExpo;

    @Column(name = "is_xmp", nullable = false)
    private boolean isXmp;

    @Column(name = "height_mm", nullable = false)
    private String heightMm;

    @Column(name = "price", nullable = false)
    private double price;
}
