package com.kaptue.dev.maintenance.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateTicketRequest {

    @NotBlank(message = "Le titre est requis")
    @Size(max = 255)
    private String titre;

    @NotBlank(message = "La description est requise")
    private String description;

    @NotNull(message = "L'ID du client est requis")
    private Long clientId;

    // L'ID utilisateur sera généralement celui de l'utilisateur authentifié,
    // mais si un admin peut créer pour un autre user :
    private Long userId; // Optionnel, sinon prendre l'utilisateur authentifié

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