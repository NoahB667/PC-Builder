package com.pcbuilder.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "motherboards")
public class Motherboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "socket", nullable = false)
    private String socket;

    @Column(name = "chipset", nullable = false)
    private String chipset;

    @Column(name = "form_factor", nullable = false)
    private String formFactor;

    @Column(name = "ram_gen", nullable = false)
    private String ramGen;

    @Column(name = "ram_slots", nullable = false)
    private Integer ramSlots;

    @Column(name = "max_ram_speed_mts")
    private Integer maxRamSpeedMts;

    @Column(name = "pcie_gen_primary")
    private String pcieGenPrimary;

    @Column(name = "m2_slots_count")
    private Integer m2SlotsCount;

    @Column(name = "has_wifi")
    private Boolean hasWifi;

    @Column(name = "wifi_version")
    private String wifiVersion;

    @Column(name = "supports_back_connect")
    private Boolean supportsBackConnect;
}
