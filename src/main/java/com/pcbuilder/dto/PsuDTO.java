package com.pcbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PsuDTO {
    private Long id;
    private String name;
    private Integer wattage;
    private String modular;
    private String certification;
    private Double price;
    private String brand;
}
