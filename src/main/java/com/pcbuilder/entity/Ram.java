package com.pcbuilder.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ram")
public class Ram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "size_gb", nullable = false)
    private int sizeGb;

    @Column(name = "sticks", nullable = false)
    private int sticks;

    @Column(name = "speed_mhz", nullable = false)
    private int speedMhz;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "brand", nullable = false)
    private String brand;

}
