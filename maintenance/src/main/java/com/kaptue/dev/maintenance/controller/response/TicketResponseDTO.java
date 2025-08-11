package com.kaptue.dev.maintenance.controller.response;

import com.kaptue.dev.maintenance.entity.Ticket;
import com.kaptue.dev.maintenance.entity.enums.TicketStatus; // Importer l'enum

import java.time.LocalDateTime;

public class TicketResponseDTO {

    private Long id;
    private String titre;
    private String description;
    private LocalDateTime dateCreation;
    private TicketStatus statut; // Utiliser l'enum
    private Integer avancementPourcentage;

    // Informations sur le client et l'utilisateur
    private Long clientId;
    private String clientNom;
    private Long userId;
    private String userNom;

    public static TicketResponseDTO fromEntity(Ticket ticket) {
        if (ticket == null) return null;

        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setId(ticket.getId());
        dto.setTitre(ticket.getTitre());
        dto.setDescription(ticket.getDescription());
        dto.setDateCreation(ticket.getDateCreation());
        dto.setStatut(ticket.getStatut());
        dto.setAvancementPourcentage(ticket.getAvancementPourcentage());

        if (ticket.getClient() != null) {
            dto.setClientId(ticket.getClient().getId());
            dto.setClientNom(ticket.getClient().getNom());
        }

        if (ticket.getUser() != null) {
            dto.setUserId(ticket.getUser().getId());
            dto.setUserNom(ticket.getUser().getNom());
        }
        
        return dto;
    }

    // Générez tous les Getters et Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public TicketStatus getStatut() { return statut; }
    public void setStatut(TicketStatus statut) { this.statut = statut; }
    public Integer getAvancementPourcentage() { return avancementPourcentage; }
    public void setAvancementPourcentage(Integer avancementPourcentage) { this.avancementPourcentage = avancementPourcentage; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public String getClientNom() { return clientNom; }
    public void setClientNom(String clientNom) { this.clientNom = clientNom; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserNom() { return userNom; }
    public void setUserNom(String userNom) { this.userNom = userNom; }
}