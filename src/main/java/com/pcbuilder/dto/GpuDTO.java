package com.pcbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GpuDTO {
    private Long id;
    private String name;
    private Integer vram; // in GB
    private Integer tdp; // Thermal Design Power in watts
    private Double price; // in USD
    private String brand; // e.g., NVIDIA, AMD
}
