package com.pcbuilder.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "storages")
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "capacity_gb", nullable = false)
    private int capacityGb;

    @Column(name = "interface_type", nullable = false)
    private String interfaceType;

    @Column(name = "form_factor", nullable = false)
    private String formFactor;

    @Column(name = "max_read_speed_mbs", nullable = false)
    private int maxReadSpeedMbs;

    @Column(name = "max_write_speed_mbs", nullable = false)
    private int maxWriteSpeedMbs;

    @Column(name = "tbw_rating", nullable = false)
    private int tbwRating;

    @Column(name = "has_dram", nullable = false)
    private boolean hasDram;

    @Column(name = "nand_type", nullable = false)
    private String nandType;

    @Column(name = "includes_heatsink", nullable = false)
    private boolean includesHeatSink;

    public double getBenchmarkScore() {
        // Normalize Speed (Range: 500 MB/s for SATA to 15,000 MB/s for Gen5)
        double avgSpeed = (this.maxReadSpeedMbs + this.maxWriteSpeedMbs) / 2.0;
        double speedScore = (avgSpeed - 500.0) / (15000.0 - 500.0) * 100;

        // Normalize Durability (TBW per GB), a good SSD usually has 0.6 TBW per 1GB of capacity.
        double durabilityRatio = (double) this.tbwRating / this.capacityGb;
        double durabilityScore = (durabilityRatio / 0.8) * 100;

        // Tech & Stability (DRAM and NAND Type)
        double techScore = 0;
        if (this.hasDram) techScore += 50;
        if (this.nandType.equalsIgnoreCase("TLC")) techScore += 50;
        else if (this.nandType.equalsIgnoreCase("MLC")) techScore += 50;
        else if (this.nandType.equalsIgnoreCase("QLC")) techScore += 20;

        speedScore = Math.max(0, Math.min(100, speedScore));
        durabilityScore = Math.max(0, Math.min(100, durabilityScore));
        return (speedScore * 0.6) + (durabilityScore * 0.2) + (techScore * 0.2);
    }
}
