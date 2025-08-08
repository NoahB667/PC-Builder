package com.pcbuilder.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "build_components")
public class BuildComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "build_id", nullable = false)
    private Long buildId;

    @Column(name = "component_id", nullable = false)
    private Long componentId;

    @Column(name = "component_type", nullable = false)
    private String componentType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

}
