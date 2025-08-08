package com.pcbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildDTO {
    private Long id;
    private Long userId;
    private String name;
    private Double budget;
    private LocalDateTime createdAt;
}
