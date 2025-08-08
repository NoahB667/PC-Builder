package com.pcbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MotherboardDTO {
    private Long id;
    private String name;
    private String socket;
    private String formFactor;
    private String chipset;
    private String ramType;
    private int ramSlots;
    private double price;
    private String brand;
}
