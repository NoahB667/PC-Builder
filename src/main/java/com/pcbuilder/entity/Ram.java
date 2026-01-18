package com.pcbuilder.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rams")
public class Ram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model_name", nullable = false)
    private String name;

    @Column(name = "generation", nullable = false)
    private String generation;

    @Column(name = "speed_mhz", nullable = false)
    private int speedMhz;

    @Column(name = "cas_latency", nullable = false)
    private int casLatency;

    @Column(name = "total_capacity_gb", nullable = false)
    private int totalCapacityGb;

    @Column(name = "num_modules", nullable = false)
    private int numModules;

    @Column(name = "is_expo", nullable = false)
    private boolean isExpo;

    @Column(name = "is_xmp", nullable = false)
    private boolean isXmp;

    @Column(name = "height_mm", nullable = false)
    private String heightMm;

    @Column(name = "price", nullable = false)
    private double price;

    public double getBenchmarkScore() {
        // Normalize Speed (4800 to 8000 MHz range)
        double speedScore = (this.speedMhz - 4800.0) / (8000.0 - 4800.0) * 100;

        // Normalize Latency (CL40 is "low", CL28 is "high" performance)
        // Formula inverted because lower CAS is better
        double latencyScore = (40.0 - this.casLatency) / (40.0 - 28.0) * 100;

        // Normalize Capacity (8GB to 128GB range)
        double capacityScore = (this.totalCapacityGb - 8.0) / (128.0 - 8.0) * 100;

        // Ensure no values go below 0 or above 100
        speedScore = Math.max(0, Math.min(100, speedScore));
        latencyScore = Math.max(0, Math.min(100, latencyScore));
        capacityScore = Math.max(0, Math.min(100, capacityScore));

        return (speedScore * 0.5) + (latencyScore * 0.3) + (capacityScore * 0.2);
    }
}
