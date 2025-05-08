package com.pcbuilder.model;

import java.util.List;

import lombok.Data;

@Data
public class BuildSuggestion {
    private List<Component> components;
    private boolean compatibilityPass;
    private double totalPrice;

    public BuildSuggestion(List<Component> components, boolean compatibilityPass, double totalPrice) {
        this.components = components;
        this.compatibilityPass = compatibilityPass;
        this.totalPrice = totalPrice;
    }
} 