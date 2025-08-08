package com.pcbuilder.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "gpus")
public class Gpu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "vram", nullable = false)
    private Integer vram; // in GB

    @Column(name = "tdp", nullable = false)
    private Integer tdp; // Thermal Design Power in watts

    @Column(name = "price", nullable = false)
    private Double price; // in USD

    @Column(name = "brand", nullable = false)
    private String brand; // e.g., NVIDIA, AMD

}
