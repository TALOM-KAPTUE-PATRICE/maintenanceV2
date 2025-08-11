package com.kaptue.dev.maintenance.service;

import com.kaptue.dev.maintenance.controller.response.NotificationResponseDTO;
import com.kaptue.dev.maintenance.entity.Notification;
import com.kaptue.dev.maintenance.entity.User;
import com.kaptue.dev.maintenance.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    // Template pour envoyer des messages via WebSocket
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Crée une notification, la sauvegarde en base de données,
     * et l'envoie à un utilisateur spécifique via WebSocket.
     * @param message Le contenu de la notification.
     * @param user L'utilisateur qui doit recevoir la notification.
     */
    @Transactional
    public void createAndSendNotification(String message, User user) {
        if (user == null || user.getEmail() == null) {
            logger.warn("Tentative d'envoi de notification à un utilisateur nul ou sans email.");
            return;
        }

        // 1. Créer et sauvegarder l'entité Notification en BDD
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUser(user);
        Notification savedNotification = notificationRepository.save(notification);

        // 2. Préparer le DTO à envoyer au client
        NotificationResponseDTO dto = NotificationResponseDTO.fromEntity(savedNotification);
        
        // 3. Envoyer le message à l'utilisateur spécifique
        // La destination est '/user/{username}/queue/notifications'.
        // Spring remplace automatiquement {username} par l'email de l'utilisateur.
        // Le client doit s'abonner à "/user/queue/notifications".
        logger.info("Envoi de la notification '{}' à l'utilisateur {}", message, user.getEmail());
        messagingTemplate.convertAndSendToUser(
            user.getEmail(), // Le 'username' de l'utilisateur (son email dans notre cas)
            "/queue/notifications", // La destination privée
            dto // L'objet à envoyer (sera converti en JSON)
        );
    }
}