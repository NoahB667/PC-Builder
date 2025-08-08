package com.pcbuilder.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cases")
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "form_factor", nullable = false)
    private String formFactor;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "brand", nullable = false)
    private String brand;
}
