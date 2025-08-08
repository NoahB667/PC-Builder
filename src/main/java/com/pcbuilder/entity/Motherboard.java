package com.pcbuilder.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "motherboards")
public class Motherboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "socket", nullable = false)
    private String socket;

    @Column(name = "form_factor", nullable = false)
    private String formFactor;

    @Column(name = "chipset", nullable = false)
    private String chipset;

    @Column(name = "ram_type", nullable = false)
    private String ramType;

    @Column(name = "ram_slots", nullable = false)
    private int ramSlots;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "brand", nullable = false)
    private String brand;

}
