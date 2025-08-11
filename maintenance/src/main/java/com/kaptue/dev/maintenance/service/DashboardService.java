package com.kaptue.dev.maintenance.service;

import com.kaptue.dev.maintenance.controller.dto.kpi.*;
import com.kaptue.dev.maintenance.entity.enums.DemandeAchatStatus;
import com.kaptue.dev.maintenance.entity.enums.DevisStatus;
import com.kaptue.dev.maintenance.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    @Autowired private TicketRepository ticketRepository;
    @Autowired private DevisRepository devisRepository;
    @Autowired private DemandeAchatRepository demandeAchatRepository;

    // KPI 1: État des projets
    public List<ProjectStatusKpiDTO> getProjectStatus() {
        return ticketRepository.findAllWithClient().stream() // Méthode à créer dans TicketRepository
            .map(ticket -> {
                ProjectStatusKpiDTO dto = new ProjectStatusKpiDTO();
                dto.setId(ticket.getId());
                dto.setTitre(ticket.getTitre());
                dto.setStatut(ticket.getStatut());
                dto.setAvancementPourcentage(ticket.getAvancementPourcentage());
                if (ticket.getClient() != null) {
                    dto.setClientNom(ticket.getClient().getNom());
                }
                return dto;
            }).collect(Collectors.toList());
    }

    // KPI 2: Devis déposés par client
    public List<ChartDataDTO<String>> getSubmittedDevisByClient() {
        return devisRepository.countByStatusAndGroupByClient(DevisStatus.SOUMIS);
    }
    
    // KPI 3: Devis validés vs non validés
    public List<ChartDataDTO<DevisStatus>> getDevisStatusCounts() {
        return devisRepository.countByStatus();
    }

    // KPI 4: Projets avec ou sans Bon de Commande Client
    public List<ChartDataDTO<String>> getProjectsWithOrWithoutClientPO() {
        long withPO = devisRepository.countByBonCommandeClientIsNotNull();
        long withoutPO = devisRepository.countByStatusAndBonCommandeClientIsNull(DevisStatus.VALIDER); // On compte les devis validés mais sans PO
        return List.of(
            new ChartDataDTO<>("Avec Bon de Commande Client", withPO),
            new ChartDataDTO<>("Sans Bon de Commande Client", withoutPO)
        );
    }

    // KPI 5: Statut des demandes d'achat
    public List<ChartDataDTO<DemandeAchatStatus>> getDemandeAchatStatusCounts() {
        return demandeAchatRepository.countByStatus();
    }
}