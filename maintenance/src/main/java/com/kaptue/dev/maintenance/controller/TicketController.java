package com.kaptue.dev.maintenance.controller;

import java.time.Month;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // Importer l'entité User
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType; // Pour obtenir l'utilisateur authentifié
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // Pour la pagination par défaut
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaptue.dev.maintenance.controller.dto.TicketDTO; // Pour obtenir l'utilisateur authentifié
import com.kaptue.dev.maintenance.controller.dto.TicketPdfDTO;
import com.kaptue.dev.maintenance.controller.request.CreateTicketRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateTicketRequest;
import com.kaptue.dev.maintenance.entity.User;
import com.kaptue.dev.maintenance.security.Permission;
import com.kaptue.dev.maintenance.service.PdfService;
import com.kaptue.dev.maintenance.service.TicketService;
import com.kaptue.dev.maintenance.service.UserService;
import com.kaptue.dev.maintenance.controller.response.TicketResponseDTO;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    @Autowired
    private TicketService ticketService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private UserService userServiceInternal; // Renommé pour éviter conflit avec entité User

    /**
     * Récupère tous les tickets (paginés).
     * Nécessite la permission TICKET_READ_ALL.
     * @param pageable Informations de pagination.
     * @return Page de TicketDTO.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('" + Permission.TICKET_READ_ALL + "') or hasAuthority('" + Permission.TICKET_READ_OWN + "')")
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets() {
        List<TicketResponseDTO> tickets = ticketService.findAllTickets();
        return ResponseEntity.ok(tickets);
    }
    

    /**
     * Crée un nouveau ticket.
     * Nécessite la permission TICKET_CREATE.
     * @param createTicketRequest DTO contenant les informations du ticket.
     * @param authentication Informations de l'utilisateur authentifié.
     * @return TicketDTO du ticket créé.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('" + Permission.TICKET_CREATE + "')")
    public ResponseEntity<TicketDTO> createTicket(@Valid @RequestBody CreateTicketRequest createTicketRequest, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User authenticatedUser = userServiceInternal.findUserEntityByEmail(userDetails.getUsername()); // Méthode à créer dans UserService qui retourne l'entité

        logger.info("Création d'un ticket par l'utilisateur: {}", authenticatedUser.getEmail());
        TicketDTO createdTicket = ticketService.createTicket(createTicketRequest, authenticatedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    /**
     * Récupère un ticket spécifique par son ID.
     * Nécessite la permission TICKET_READ ou TICKET_READ_ALL.
     * TODO: Implémenter une vérification plus fine si TICKET_READ (l'utilisateur peut-il voir CE ticket ?).
     * @param id L'ID du ticket.
     * @return TicketDTO.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Permission.TICKET_READ_OWN + "') or hasAuthority('" + Permission.TICKET_READ_ALL + "')")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        logger.info("Récupération du ticket avec ID: {}", id);
        TicketDTO ticket = ticketService.findTicketById(id);
        return ResponseEntity.ok(ticket);
    }

    /**
     * Met à jour un ticket existant.
     * Nécessite la permission TICKET_UPDATE.
     * TODO: Implémenter une vérification plus fine (l'utilisateur peut-il modifier CE ticket ?).
     * @param id L'ID du ticket.
     * @param updateTicketRequest DTO avec les informations de mise à jour.
     * @param authentication Informations de l'utilisateur authentifié.
     * @return TicketDTO mis à jour.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Permission.TICKET_UPDATE + "')")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id,
                                                @Valid @RequestBody UpdateTicketRequest updateTicketRequest,
                                                Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User authenticatedUser = userServiceInternal.findUserEntityByEmail(userDetails.getUsername());

        logger.info("Mise à jour du ticket ID: {} par l'utilisateur: {}", id, authenticatedUser.getEmail());
        TicketDTO updatedTicket = ticketService.updateTicket(id, updateTicketRequest, authenticatedUser);
        return ResponseEntity.ok(updatedTicket);
    }

    /**
     * Supprime un ticket.
     * Nécessite la permission TICKET_DELETE.
     * @param id L'ID du ticket à supprimer.
     * @return ResponseEntity sans contenu.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Permission.TICKET_DELETE + "')")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        logger.info("Suppression du ticket ID: {}", id);
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Génère un PDF pour un ticket spécifique.
     * Nécessite la permission TICKET_READ ou TICKET_READ_ALL.
     * @param id L'ID du ticket.
     * @return ResponseEntity avec le contenu PDF.
     */
       /**
     * Génère un PDF pour un ticket spécifique.
     * L'accès est autorisé si l'utilisateur a la permission TICKET_READ_ALL, OU
     * s'il est autorisé par le TicketSecurityService à lire ce ticket spécifique.
     * @param id L'ID du ticket.
     * @param authentication L'objet d'authentification pour les vérifications de sécurité.
     * @return ResponseEntity avec le contenu PDF.
     */
    
    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasAuthority('" + Permission.TICKET_READ_ALL + "') or @ticketSecurityService.canReadTicket(authentication, #id)")
    public ResponseEntity<byte[]> generateTicketPdf(@PathVariable Long id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        logger.info("Demande de génération PDF pour le ticket ID: {} par l'utilisateur: {}", id, userDetails.getUsername());

        // 1. Préparer les données via le service métier (transactionnel)
        TicketPdfDTO ticketData = ticketService.prepareTicketDataForPdf(id);

        // 2. Générer le PDF avec les données préparées
        byte[] pdfContents = pdfService.generateTicketPdf(ticketData);

        if (pdfContents == null || pdfContents.length == 0) {
            logger.error("Échec de la génération du PDF pour le ticket ID: {}", id);
            return ResponseEntity.internalServerError().body("Erreur interne lors de la génération du PDF.".getBytes());
        }

        HttpHeaders headers = new HttpHeaders();
        // "inline" pour l'afficher dans le navigateur, "attachment" pour forcer le téléchargement
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=ticket-" + id + ".pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);

        return ResponseEntity.ok().headers(headers).body(pdfContents);
    }

    // Endpoints statistiques (similaires à avant, mais avec permissions)
    @GetMapping("/stats/countByMonth")
    @PreAuthorize("hasAuthority('" + Permission.TICKET_READ_ALL + "')") // Ou une permission de reporting
    public ResponseEntity<Map<Month, Long>> getCountTicketsByMonth() {
        return ResponseEntity.ok(ticketService.countTicketsByMonth());
    }

    @GetMapping("/stats/countPreviousMonth")
    @PreAuthorize("hasAuthority('" + Permission.TICKET_READ_ALL + "')")
    public ResponseEntity<Long> getCountTicketsForPreviousMonth() {
        return ResponseEntity.ok(ticketService.countTicketsForPreviousMonth());
    }
}

