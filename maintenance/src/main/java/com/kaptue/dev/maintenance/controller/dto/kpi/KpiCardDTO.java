package com.kaptue.dev.maintenance.controller.dto.kpi;

public class KpiCardDTO {
    private String title;
    private Long value;
    // Getters, Setters, constructeur


    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getValue() {
        return this.value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

}
