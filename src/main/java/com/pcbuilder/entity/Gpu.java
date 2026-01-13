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

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "chipset_brand", nullable = false)
    private String chipset_brand;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "vram_gb", nullable = false)
    private Integer vramGb;

    @Column(name = "vram_type", nullable = false)
    private Integer vramType;

    @Column(name = "boost_clock_mhz", nullable = false)
    private Integer boostClockMhz;

    @Column(name = "performance_score", nullable = false)
    private Integer performanceScore;

    @Column(name = "tdp", nullable = false)
    private Integer tdp;

    @Column(name = "length_mm", nullable = false)
    private Integer lengthMm;

    @Column(name = "slot_width", nullable = false)
    private Integer slotWidth;

    @Column(name = "pcie_gen", nullable = false)
    private Integer pcieGen;
}
