package com.pcbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseDTO {
    private Long id;
    private String name;
    private String formFactor;
    private Double price;
    private String brand;
}
