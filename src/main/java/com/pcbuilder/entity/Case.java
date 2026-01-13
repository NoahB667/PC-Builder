package com.pcbuilder.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cases")
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "color")
    private String color;

    @Column(name = "type")
    private String type;

    @Column(name = "max_motherboard_size")
    private String maxMotherboardSize;

    @Column(name = "psu_form_factor")
    private String psuFormFactor;

    @Column(name = "max_gpu_length_mm")
    private Integer maxGpuLengthMm;

    @Column(name = "max_cpu_cooler_height_mm")
    private Integer maxCpuCoolerHeightMm;

    @Column(name = "max_radiator_support_mm")
    private Integer maxRadiatorSupportMm;

    @Column(name = "has_tempered_glass")
    private Boolean hasTemperedGlass;

    @Column(name = "supports_back_connect")
    private Boolean supportsBackConnect;

    @Column(name = "usb_c_front_panel")
    private Boolean usbCFrontPanel;
}
