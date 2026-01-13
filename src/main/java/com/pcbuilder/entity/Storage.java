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
}
