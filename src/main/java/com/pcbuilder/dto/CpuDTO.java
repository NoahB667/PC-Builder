package com.pcbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CpuDTO {
    private Long id;
    private String name;
    private String socket;
    private Integer cores;
    private Integer threads;
    private Double baseClock;
    private Double boostClock;
    private Integer tdp;
    private Double price;
    private String brand;
}
