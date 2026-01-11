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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "capacity_gb", nullable = false)
    private int capacityGb;

    @Column(name = "speed_mbps", nullable = false)
    private int speedMbps;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "brand", nullable = false)
    private String brand;

}
