package com.pcbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RamDTO {
    private Long id;
    private String name;
    private int sizeGb;
    private int sticks;
    private int speedMhz;
    private String type;
    private double price;
    private String brand;
}
