package com.kaptue.dev.maintenance.service;

import com.kaptue.dev.maintenance.controller.request.CreateTicketRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateTicketRequest;
import com.kaptue.dev.maintenance.controller.dto.TicketDTO;
import com.kaptue.dev.maintenance.controller.dto.TicketPdfDTO;
import com.kaptue.dev.maintenance.entity.Client;
import com.kaptue.dev.maintenance.entity.Ticket;
import com.kaptue.dev.maintenance.entity.User;
import com.kaptue.dev.maintenance.exception.ResourceNotFoundException;
import com.kaptue.dev.maintenance.repository.ClientRepository;
import com.kaptue.dev.maintenance.repository.TicketRepository;
import com.kaptue.dev.maintenance.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.kaptue.dev.maintenance.controller.response.TicketResponseDTO;



@Service
public class TicketService {
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository; // Pour charger l'utilisateur

    @Autowired
    private ClientRepository clientRepository; // Pour charger le client

    @Autowired
    private NotificationService notificationService; // Pour créer des notifications

    /**
     * Récupère tous les tickets et les convertit en DTOs.
     * @return Liste de TicketDTO.
     */
    @Transactional(readOnly = true)
    public List<TicketResponseDTO> findAllTickets() {
        return ticketRepository.findAll().stream()
                .map(TicketResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Récupère un ticket par son ID.
     * @param id L'ID du ticket.
     * @return TicketDTO.
     * @throws ResourceNotFoundException si le ticket n'est pas trouvé.
     */
    public TicketDTO findTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket non trouvé avec l'ID: " + id));
        return TicketDTO.fromEntity(ticket);
    }

    /**
     * Crée un nouveau ticket.
     * @param request Les données pour créer le ticket.
     * @param authenticatedUser L'utilisateur authentifié qui crée le ticket.
     * @return Le TicketDTO du ticket créé.
     * @throws ResourceNotFoundException si le client ou l'utilisateur assigné (si fourni) n'est pas trouvé.
     */
    @Transactional
    public TicketDTO createTicket(CreateTicketRequest request, User authenticatedUser) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + request.getClientId()));

        User userToAssign;
        if (request.getUserId() != null) { // Si un utilisateur spécifique est assigné à la création
            userToAssign = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur à assigner non trouvé avec l'ID: " + request.getUserId()));
        } else {
            userToAssign = authenticatedUser; // Par défaut, assigner à l'utilisateur qui crée
        }

        Ticket ticket = new Ticket();
        ticket.setTitre(request.getTitre());
        ticket.setDescription(request.getDescription());
        ticket.setClient(client);
        ticket.setUser(userToAssign);
        // dateCreation est définie dans le constructeur de Ticket

        Ticket savedTicket = ticketRepository.save(ticket);
        logger.info("Ticket créé: ID {}", savedTicket.getId());

        // Créer une notification
        String message = "Nouveau ticket '" + savedTicket.getTitre() + "' créé et assigné à " + userToAssign.getNom();
        notificationService.createAndSendNotification(message, userToAssign); // Notifier l'utilisateur assigné
        if (!authenticatedUser.getId().equals(userToAssign.getId())) { // Notifier aussi le créateur si différent
             notificationService.createAndSendNotification("Vous avez créé le ticket '" + savedTicket.getTitre() + "' pour " + userToAssign.getNom(), authenticatedUser);
        }


        return TicketDTO.fromEntity(savedTicket);
    }


    /**
     * Met à jour un ticket existant.
     * @param id L'ID du ticket à mettre à jour.
     * @param request Les nouvelles informations du ticket.
     * @param authenticatedUser L'utilisateur authentifié qui effectue la mise à jour.
     * @return Le TicketDTO du ticket mis à jour.
     * @throws ResourceNotFoundException si le ticket, le client ou l'utilisateur n'est pas trouvé.
     */
    @Transactional
    public TicketDTO updateTicket(Long id, UpdateTicketRequest request, User authenticatedUser) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket non trouvé avec l'ID: " + id));

        // Mettre à jour les champs si présents dans la requête
        if (request.getTitre() != null) {
            ticket.setTitre(request.getTitre());
        }
        if (request.getDescription() != null) {
            ticket.setDescription(request.getDescription());
        }
        if (request.getClientId() != null) {
            Client client = clientRepository.findById(request.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + request.getClientId()));
            ticket.setClient(client);
        }
        if (request.getUserId() != null) { // Si on permet de réassigner
            User userToAssign = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur à assigner non trouvé avec l'ID: " + request.getUserId()));
            ticket.setUser(userToAssign);
        }
        // On ne met pas à jour dateCreation

        Ticket updatedTicket = ticketRepository.save(ticket);
        logger.info("Ticket mis à jour: ID {}", updatedTicket.getId());

        // Notification
        String message = "Le ticket '" + updatedTicket.getTitre() + "' a été mis à jour par " + authenticatedUser.getNom();
        // Notifier les parties concernées (ex: utilisateur assigné, créateur original si différent)
        Set<User> usersToNotify = new HashSet<>();
        usersToNotify.add(updatedTicket.getUser()); // L'assigné
        // Si vous stockez le créateur original, ajoutez-le aussi.
        usersToNotify.forEach(u -> notificationService.createAndSendNotification(message, u));


        return TicketDTO.fromEntity(updatedTicket);
    }

    /**
     * Supprime un ticket par son ID.
     * @param id L'ID du ticket à supprimer.
     * @throws ResourceNotFoundException si le ticket n'est pas trouvé.
     */
    @Transactional
    public void deleteTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket non trouvé avec l'ID: " + id + " pour suppression."));

        // Avant de supprimer, on peut vouloir notifier
        User userToNotify = ticket.getUser();
        String ticketTitre = ticket.getTitre();

        ticketRepository.deleteById(id);
        logger.info("Ticket supprimé: ID {}", id);

        if (userToNotify != null) {
            String message = "Le ticket '" + ticketTitre + "' auquel vous étiez assigné a été supprimé.";
            notificationService.createAndSendNotification(message, userToNotify);
        }
    }


    // Les méthodes de comptage sont correctes.
    public Map<Month, Long> countTicketsByMonth() {
        // La logique actuelle est OK, mais peut être optimisée avec une requête JPQL group by si la performance devient un enjeu
        return ticketRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        ticket -> ticket.getDateCreation().getMonth(),
                        Collectors.counting()
                ));
    }

    public Long countTicketsForCurrentMonth() {
        LocalDate now = LocalDate.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = now.withDayOfMonth(now.lengthOfMonth()).atTime(23, 59, 59);
        return ticketRepository.countByDateCreationBetween(startOfMonth, endOfMonth);
    }

    public Long countTicketsForPreviousMonth() {
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfPreviousMonth = now.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfPreviousMonth = now.minusMonths(1).withDayOfMonth(now.minusMonths(1).lengthOfMonth());
        LocalDateTime startOfPreviousMonth = firstDayOfPreviousMonth.atStartOfDay();
        LocalDateTime endOfPreviousMonth = lastDayOfPreviousMonth.atTime(23, 59, 59);
        return ticketRepository.countByDateCreationBetween(startOfPreviousMonth, endOfPreviousMonth);
    }

      /**
     * Prépare les données pour la génération du PDF d'un ticket.
     * Cette méthode s'exécute dans une transaction pour garantir que toutes les relations
     * lazy-loaded (comme user et client) sont accessibles.
     * @param id L'ID du ticket.
     * @return un TicketPdfDTO contenant toutes les informations nécessaires.
     */
    @Transactional(readOnly = true)
    public TicketPdfDTO prepareTicketDataForPdf(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket non trouvé avec l'ID: " + id));
        // L'accès aux relations (ticket.getUser(), ticket.getClient()) se fait ici,
        // à l'intérieur de la transaction, chargeant les données lazy.
        return TicketPdfDTO.fromEntity(ticket);
    }

    // findById interne pour le service, retourne l'entité
    public Ticket findTicketEntityById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket non trouvé avec l'ID: " + id));
    }
}