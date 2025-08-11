package com.kaptue.dev.maintenance.controller.response;

import com.kaptue.dev.maintenance.entity.Notification;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) pour représenter une notification envoyée au client.
 * Il sert à découpler la représentation de l'API de l'entité JPA.
 */
public class NotificationResponseDTO {

    private Long id;
    private String message;
    private LocalDateTime date;
    private Long userId; // ID de l'utilisateur concerné

    // Constructeur vide (utile pour certaines librairies de sérialisation)
    public NotificationResponseDTO() {
    }

    /**
     * Méthode "factory" statique pour créer un DTO à partir d'une entité Notification.
     * C'est une bonne pratique pour garder la logique de conversion au même endroit.
     * @param notification L'entité Notification à convertir.
     * @return Le DTO correspondant.
     */
    public static NotificationResponseDTO fromEntity(Notification notification) {
        if (notification == null) {
            return null;
        }

        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setMessage(notification.getMessage());
        dto.setDate(notification.getDate());

        // On expose seulement l'ID de l'utilisateur, pas l'objet User complet
        if (notification.getUser() != null) {
            dto.setUserId(notification.getUser().getId());
        }

        return dto;
    }

    // --- Getters et Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}