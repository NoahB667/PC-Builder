package com.pcbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageDTO {
    private Long id;
    private String name;
    private String type; // e.g., SSD, HDD
    private int capacityGb; // Storage capacity in GB
    private int speedMbps; // Speed in MB/s
    private double price; // Price of the storage device
    private String brand; // Brand of the storage device
}
