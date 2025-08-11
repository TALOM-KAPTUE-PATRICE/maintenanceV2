package com.kaptue.dev.maintenance.controller.request;

import jakarta.validation.constraints.Size;
// Tous les champs sont optionnels pour une mise à jour partielle
public class UpdateTicketRequest {

    @Size(max = 255, message = "Le titre ne doit pas dépasser 255 caractères")
    private String titre;

    private String description;

    private Long clientId;

    private Long userId; // Pour la réassignation

    // Getters et Setters
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}