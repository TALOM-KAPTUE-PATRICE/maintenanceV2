package com.kaptue.dev.maintenance.controller.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateStatusRequest {
    
    @NotBlank(message = "Le nouveau statut ne peut pas être vide.")
    private String status;

    private Integer percentage; // Champ optionnel pour le pourcentage d'avancement du ticket

    // Getters et Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getPercentage() { return percentage; }
    public void setPercentage(Integer percentage) { this.percentage = percentage; }
}