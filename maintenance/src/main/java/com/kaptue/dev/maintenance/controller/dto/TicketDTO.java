package com.kaptue.dev.maintenance.controller.dto;
import com.kaptue.dev.maintenance.entity.Ticket;
import java.time.LocalDateTime;

public class TicketDTO {
    private Long id;
    private String titre;
    private String description;
    private LocalDateTime dateCreation;
    private String userEmail;
    private String clientNom; // Pour afficher le nom du client
    private String userNom; // Pour afficher le nom de l'utilisateur

    // Constructeur vide pour la désérialisation
    public TicketDTO() {}

    // Constructeur pour la conversion depuis l'entité
    public TicketDTO(Long id, String titre, String description, LocalDateTime dateCreation, String userEmail, String userNom, String clientNom) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dateCreation = dateCreation;
        this.userEmail = userEmail;
        this.userNom = userNom;
        this.clientNom = clientNom;
    }

    // Méthode factory pour convertir une entité Ticket en TicketDTO
    public static TicketDTO fromEntity(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        return new TicketDTO(
            ticket.getId(),
            ticket.getTitre(),
            ticket.getDescription(),
            ticket.getDateCreation(),
            ticket.getUser() != null ? ticket.getUser().getEmail() : "N/A",
            ticket.getUser() != null ? ticket.getUser().getNom() : "N/A",
            ticket.getClient() != null ? ticket.getClient().getNom() : "N/A"
        );
    }

    // Getters (Setters optionnels pour les DTO de réponse)
    public Long getId() { return id; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public String getUserEmail() { return userEmail; }
    public String getClientNom() { return clientNom; }
    public String getUserNom() { return userNom; }

    public void setId(Long id) { this.id = id; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setDescription(String description) { this.description = description; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public void setClientNom(String clientNom) { this.clientNom = clientNom; }
    public void setUserNom(String userNom) { this.userNom = userNom; }
}