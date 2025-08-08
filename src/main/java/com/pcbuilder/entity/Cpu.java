package com.pcbuilder.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cpus")
public class Cpu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "socket", nullable = false)
    private String socket;

    @Column(name = "cores", nullable = false)
    private Integer cores;

    @Column(name = "threads", nullable = false)
    private Integer threads;

    @Column(name = "base_clock", nullable = false)
    private Double baseClock;

    @Column(name = "boost_clock", nullable = false)
    private Double boostClock;

    @Column(name = "tdp", nullable = false)
    private Integer tdp;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "brand", nullable = false)
    private String brand;

}
