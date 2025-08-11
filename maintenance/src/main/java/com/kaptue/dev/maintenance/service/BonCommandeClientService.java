package com.kaptue.dev.maintenance.service;

import com.kaptue.dev.maintenance.controller.request.CreateBonCommandeClientRequest;
import com.kaptue.dev.maintenance.controller.response.BonCommandeClientResponseDTO;
import com.kaptue.dev.maintenance.entity.BonCommandeClient;
import com.kaptue.dev.maintenance.entity.Devis;
import com.kaptue.dev.maintenance.entity.enums.DevisStatus;
import com.kaptue.dev.maintenance.exception.BadRequestException;
import com.kaptue.dev.maintenance.exception.ResourceNotFoundException;
import com.kaptue.dev.maintenance.repository.BonCommandeClientRepository;
import com.kaptue.dev.maintenance.repository.DevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BonCommandeClientService {

    @Autowired private BonCommandeClientRepository bccRepository;
    @Autowired private DevisRepository devisRepository;
    @Autowired private FileStorageService fileStorageService;

    @Transactional
    public BonCommandeClientResponseDTO create(CreateBonCommandeClientRequest request, String pdfPath) {
        Devis devis = devisRepository.findById(request.getDevisId())
                .orElseThrow(() -> new ResourceNotFoundException("Devis non trouvé avec l'ID: " + request.getDevisId()));

        if (devis.getBonCommandeClient() != null) {
            throw new BadRequestException("Ce devis est déjà associé à un autre bon de commande client.");
        }

        BonCommandeClient bcc = new BonCommandeClient();
        bcc.setReferenceClient(request.getReferenceClient());
        bcc.setDateReception(request.getDateReception());
        bcc.setNotes(request.getNotes());
        bcc.setPdfPath(pdfPath);
        bcc.setDevis(devis);

        // Règle métier : la réception d'un BC Client valide automatiquement le devis.
        devis.setStatut(DevisStatus.VALIDER);
        devisRepository.save(devis);

        BonCommandeClient savedBcc = bccRepository.save(bcc);
        return BonCommandeClientResponseDTO.fromEntity(savedBcc, fileStorageService.getFileStorageBaseUrl());
    }

    @Transactional(readOnly = true)
    public List<BonCommandeClientResponseDTO> findAll() {
        String baseUrl = fileStorageService.getFileStorageBaseUrl();
        
        return bccRepository.findAll().stream()
                .map(bcc -> BonCommandeClientResponseDTO.fromEntity(bcc, baseUrl))
                .collect(Collectors.toList());
    }

        // ▼▼▼ AJOUTEZ CES MÉTHODES ▼▼▼
    @Transactional(readOnly = true)
    public BonCommandeClientResponseDTO findById(Long id) {
        String baseUrl = fileStorageService.getFileStorageBaseUrl();
        BonCommandeClient bcc = bccRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bon de commande client non trouvé avec l'ID: " + id));
        return BonCommandeClientResponseDTO.fromEntity(bcc, baseUrl);
    }

    @Transactional
    public void delete(Long id) {
        BonCommandeClient bcc = bccRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bon de commande client non trouvé avec l'ID: " + id));
        
        // Supprimer le fichier PDF associé avant de supprimer l'entité
        if (bcc.getPdfPath() != null) {
            fileStorageService.deleteFile(bcc.getPdfPath());
        }
        
        bccRepository.delete(bcc);
    }

    
}