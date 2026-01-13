package com.pcbuilder.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "psus")
public class Psu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "wattage", nullable = false)
    private Integer wattage;

    @Column(name = "efficiency_rating", nullable = false)
    private String efficiencyRating;

    @Column(name = "modularity", nullable = false)
    private String modularity;

    @Column(name = "form_factor", nullable = false)
    private String formFactor;

    @Column(name = "atx_version")
    private String atxVersion;

    @Column(name = "has_12v2x6")
    private Boolean has12v2x6;

    @Column(name = "pcie_5_1_ready")
    private Boolean pcie51Ready;
}