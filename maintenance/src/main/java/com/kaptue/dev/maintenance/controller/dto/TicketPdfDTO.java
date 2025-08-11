package com.kaptue.dev.maintenance.controller.dto;

import com.kaptue.dev.maintenance.entity.Ticket;
import java.time.LocalDateTime;

/**
 * DTO transportant les données nécessaires à la génération du PDF pour un Ticket.
 */
public class TicketPdfDTO {
    private Long id;
    private String titre;
    private String description;
    private LocalDateTime dateCreation;
    private String statut; // Vous pourriez ajouter un champ statut au ticket

    // Infos Client
    private String clientNom;
    private String clientEmail;
    private String clientNumero;

    // Infos User (demandeur/intervenant)
    private String userNom;
    private String userEmail;
    private String userPoste;

    // Constructeur vide pour la sérialisation
    public TicketPdfDTO() {}

    // Méthode factory pour construire le DTO à partir de l'entité Ticket
    public static TicketPdfDTO fromEntity(Ticket ticket) {
        if (ticket == null) return null;

        TicketPdfDTO dto = new TicketPdfDTO();
        dto.setId(ticket.getId());
        dto.setTitre(ticket.getTitre());
        dto.setDescription(ticket.getDescription());
        dto.setDateCreation(ticket.getDateCreation());
        // dto.setStatut(ticket.getStatut()); // Décommentez si vous avez un champ statut

        if (ticket.getClient() != null) {
            dto.setClientNom(ticket.getClient().getNom());
            dto.setClientEmail(ticket.getClient().getEmail());
            dto.setClientNumero(ticket.getClient().getNumero());
        }

        if (ticket.getUser() != null) {
            dto.setUserNom(ticket.getUser().getNom());
            dto.setUserEmail(ticket.getUser().getEmail());
            dto.setUserPoste(ticket.getUser().getPoste());
        }

        return dto;
    }

    // Getters et Setters pour tous les champs
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getClientNom() { return clientNom; }
    public void setClientNom(String clientNom) { this.clientNom = clientNom; }
    public String getClientEmail() { return clientEmail; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }
    public String getClientNumero() { return clientNumero; }
    public void setClientNumero(String clientNumero) { this.clientNumero = clientNumero; }
    public String getUserNom() { return userNom; }
    public void setUserNom(String userNom) { this.userNom = userNom; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getUserPoste() { return userPoste; }
    public void setUserPoste(String userPoste) { this.userPoste = userPoste; }
}