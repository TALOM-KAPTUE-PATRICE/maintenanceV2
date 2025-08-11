package com.kaptue.dev.maintenance.security;

import com.kaptue.dev.maintenance.entity.Ticket;
import com.kaptue.dev.maintenance.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service pour les vérifications de sécurité personnalisées liées aux tickets.
 * Peut être appelé depuis les annotations @PreAuthorize.
 */
@Service("ticketSecurityService") // Le nom 'ticketSecurityService' est important pour SpEL
public class TicketSecurityService {

    @Autowired
    private TicketRepository ticketRepository;

    /**
     * Vérifie si l'utilisateur authentifié a le droit de lire un ticket spécifique.
     * C'est le cas s'il est l'utilisateur assigné au ticket.
     * @param authentication L'objet d'authentification de Spring Security.
     * @param ticketId L'ID du ticket à vérifier.
     * @return true si l'utilisateur est autorisé, false sinon.
     */
    public boolean canReadTicket(Authentication authentication, Long ticketId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String currentUsername = authentication.getName(); // C'est l'email de l'utilisateur
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);

        if (ticketOpt.isEmpty()) {
            return true; // Laisser le contrôleur gérer le 404 Not Found, ne pas bloquer pour cause de sécurité
        }

        Ticket ticket = ticketOpt.get();
        // L'utilisateur peut lire le ticket s'il en est le demandeur/assigné.
        return ticket.getUser() != null && ticket.getUser().getEmail().equals(currentUsername);
    }
    
}