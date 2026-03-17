package com.mytransport.models.carlog;

public enum FuelType {
    PETROL("Бензин"),
    DIESEL("Дизел"),
    LPG("Газ"),
    ELECTRIC("Ток"),
    HYBRID("Хибрид");

    private final String label;

    FuelType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
