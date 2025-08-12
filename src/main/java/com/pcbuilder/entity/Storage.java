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
    private String type; // e.g., SSD, HDD

    @Column(name = "capacity_gb", nullable = false)
    private int capacityGb; // Storage capacity in GB

    @Column(name = "speed_mbps", nullable = false)
    private int speedMbps; // Speed in MB/s

    @Column(name = "price", nullable = false)
    private double price; // Price of the storage device

    @Column(name = "brand", nullable = false)
    private String brand; // Brand of the storage device


}
