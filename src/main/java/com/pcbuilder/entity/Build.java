package com.pcbuilder.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "builds")
public class Build {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(name = "budget")
    private Double budget;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

}
