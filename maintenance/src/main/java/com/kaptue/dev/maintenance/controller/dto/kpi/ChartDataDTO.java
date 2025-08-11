package com.kaptue.dev.maintenance.controller.dto.kpi;

public class ChartDataDTO<T> {
    private T label;
    private Long value;

        // ▼▼▼ AJOUTEZ CE CONSTRUCTEUR ▼▼▼
    public ChartDataDTO(T label, Long value) {
        this.label = label;
        this.value = value;
    }
    
    // Getters, Setters, constructeur

    public T getLabel() {
        return this.label;
    }

    public void setLabel(T label) {
        this.label = label;
    }

    public Long getValue() {
        return this.value;
    }

    public void setValue(Long value) {
        this.value = value;
    }




}