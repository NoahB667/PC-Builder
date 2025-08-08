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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "wattage", nullable = false)
    private Integer wattage;

    @Column(name = "modular", nullable = false)
    private String modular;

    @Column(name = "certification", nullable = false)
    private String certification;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "brand", nullable = false)
    private String brand;
}
