package com.kaptue.dev.maintenance.service;


import com.kaptue.dev.maintenance.controller.request.CreateDemandeAchatRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateDemandeAchatRequest;
import com.kaptue.dev.maintenance.controller.response.DemandeAchatResponseDTO;
import com.kaptue.dev.maintenance.entity.DemandeAchat;
import com.kaptue.dev.maintenance.entity.Devis;
import com.kaptue.dev.maintenance.entity.Ticket;
import com.kaptue.dev.maintenance.entity.User;
import com.kaptue.dev.maintenance.entity.enums.DemandeAchatStatus;
import com.kaptue.dev.maintenance.exception.BadRequestException;
import com.kaptue.dev.maintenance.exception.ResourceNotFoundException;
import com.kaptue.dev.maintenance.repository.DemandeAchatRepository;
import com.kaptue.dev.maintenance.repository.DevisRepository;
import com.kaptue.dev.maintenance.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kaptue.dev.maintenance.controller.dto.DemandeAchatPdfDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemandeAchatService {
    private static final Logger logger = LoggerFactory.getLogger(DemandeAchatService.class);

    @Autowired
    private DemandeAchatRepository demandeAchatRepository;

    @Autowired
    private TicketRepository ticketRepository; // Pour charger l'entité Ticket

    @Autowired
    private DevisRepository devisRepository;   // Pour charger l'entité Devis


    @Transactional
    public DemandeAchatResponseDTO createDemandeAchat(CreateDemandeAchatRequest request, User initiateur) {
        Ticket ticket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket non trouvé avec l'ID: " + request.getTicketId()));
        Devis devis = devisRepository.findById(request.getDevisId())
                .orElseThrow(() -> new ResourceNotFoundException("Devis non trouvé avec l'ID: " + request.getDevisId()));

        DemandeAchat demandeAchat = new DemandeAchat();
        
        // ▼▼▼ CORRECTION ICI ▼▼▼
        // On utilise le nom de l'utilisateur authentifié, et non plus celui de la requête.
        demandeAchat.setNomInitiateur(initiateur.getNom());
        
        demandeAchat.setLieuLivraison(request.getLieuLivraison());
        demandeAchat.setServiceDemandeur(request.getServiceDemandeur());
        demandeAchat.setObjet(request.getObjet());
        demandeAchat.setTicket(ticket);
        demandeAchat.setDevis(devis);
        demandeAchat.setDateUpdateDa(LocalDate.now());

        DemandeAchat savedDemandeAchat = demandeAchatRepository.save(demandeAchat);
        logger.info("Demande d'achat créée ID: {} par {}", savedDemandeAchat.getId(), initiateur.getEmail());
        
        return DemandeAchatResponseDTO.fromEntity(savedDemandeAchat);
    }

    public List<DemandeAchatResponseDTO> findAllDemandesAchats() {
        return demandeAchatRepository.findAll().stream()
                .map(DemandeAchatResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public DemandeAchatResponseDTO findDemandeAchatById(Long id) {
        DemandeAchat da = demandeAchatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande d'achat non trouvée avec l'ID: " + id));
        return DemandeAchatResponseDTO.fromEntity(da);
    }

    // Méthode pour récupérer l'entité (usage interne)
    public DemandeAchat findDemandeAchatEntityById(Long id) {
        return demandeAchatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande d'achat non trouvée avec l'ID: " + id));
    }

    @Transactional
    public DemandeAchatResponseDTO updateDemandeAchat(Long id, UpdateDemandeAchatRequest request) {
        DemandeAchat da = demandeAchatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande d'achat non trouvée avec l'ID: " + id));

        // Règle métier : On ne peut modifier une DA que si elle est 'EN_ATTENTE'
        if (da.getStatut() != DemandeAchatStatus.EN_ATTENTE) {
            throw new BadRequestException("Impossible de modifier une demande d'achat qui a déjà été traitée.");
        }

        da.setLieuLivraison(request.getLieuLivraison());
        da.setServiceDemandeur(request.getServiceDemandeur());
        da.setObjet(request.getObjet());
        da.setDateUpdateDa(LocalDate.now());

        DemandeAchat updatedDemandeAchat = demandeAchatRepository.save(da);
        logger.info("Demande d'achat ID: {} mise à jour.", updatedDemandeAchat.getId());
        return DemandeAchatResponseDTO.fromEntity(updatedDemandeAchat);
    }

    @Transactional
    public void deleteDemandeAchat(Long id) {
        if (!demandeAchatRepository.existsById(id)) {
            throw new ResourceNotFoundException("Demande d'achat non trouvée avec l'ID: " + id + " pour suppression.");
        }
        demandeAchatRepository.deleteById(id);
        logger.info("Demande d'achat supprimée ID: {}", id);
    }

        /**
     * Prépare les données pour la génération du PDF d'une demande d'achat.
     * S'exécute dans une transaction pour charger toutes les données nécessaires.
     * @param id L'ID de la demande d'achat.
     * @param initiateur L'utilisateur qui a fait la demande (pour le nom et le poste).
     * @return un DemandeAchatPdfDTO prêt à être utilisé par le template.
     */
    @Transactional(readOnly = true)
    public DemandeAchatPdfDTO prepareDemandeAchatDataForPdf(Long id, User initiateur) {
        DemandeAchat demandeAchat = demandeAchatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande d'achat non trouvée avec l'ID: " + id));
        
        // La magie de la transaction : les accès aux relations ci-dessous fonctionnent.
        // ex: demandeAchat.getDevis().getArticles()
        
        logger.info("Préparation des données PDF pour la DA ID: {}", id);
        return DemandeAchatPdfDTO.fromEntity(demandeAchat, initiateur);
    }

        @Transactional
    public DemandeAchatResponseDTO updateDemandeAchatStatus(Long id, String newStatus) {
        DemandeAchat demandeAchat = demandeAchatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande d'achat non trouvée avec l'ID: " + id));

        DemandeAchatStatus statusEnum;
        try {
            statusEnum = DemandeAchatStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Statut invalide pour une demande d'achat: " + newStatus);
        }

        demandeAchat.setStatut(statusEnum);
        DemandeAchat updatedDemandeAchat = demandeAchatRepository.save(demandeAchat);
        logger.info("Statut de la Demande d'Achat ID {} mis à jour à {}", id, newStatus);
        
        // Assurez-vous que votre DemandeAchatResponseDTO expose le champ 'statut'
        return DemandeAchatResponseDTO.fromEntity(updatedDemandeAchat);
    }
    
}