package com.kaptue.dev.maintenance.service;

import com.kaptue.dev.maintenance.controller.dto.FacturePdfDTO;
import com.kaptue.dev.maintenance.controller.request.CreateFactureRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateFactureRequest;
import com.kaptue.dev.maintenance.controller.response.FactureResponseDTO;
import com.kaptue.dev.maintenance.entity.Client;
import com.kaptue.dev.maintenance.entity.Devis;
import com.kaptue.dev.maintenance.entity.Facture;
import com.kaptue.dev.maintenance.exception.ResourceNotFoundException;
import com.kaptue.dev.maintenance.repository.ClientRepository;
import com.kaptue.dev.maintenance.repository.DevisRepository;
import com.kaptue.dev.maintenance.repository.FactureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FactureService {
    private static final Logger logger = LoggerFactory.getLogger(FactureService.class);

    @Autowired private FactureRepository factureRepository;
    @Autowired private DevisRepository devisRepository;
    @Autowired private ClientRepository clientRepository;

    @Transactional
    public FactureResponseDTO createFacture(CreateFactureRequest request) {
        Devis devis = devisRepository.findById(request.getDevisId())
                .orElseThrow(() -> new ResourceNotFoundException("Devis non trouvé avec l'ID: " + request.getDevisId()));
        
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + request.getClientId()));

        Facture facture = new Facture();
        facture.setId(generateNextId());
        facture.setDateFacturation(LocalDate.now());
        facture.setDateUpdateFacture(LocalDate.now());

        // Calculer le montant total à partir des articles du devis
        double montant = devis.getArticles().stream()
                .mapToDouble(article -> article.getQuantite() * article.getPrixUnitaire())
                .sum();
        facture.setMontant(montant);

        // Mapper depuis la requête
        facture.setNumeroSecretariat(request.getNumeroSecretariat());
        facture.setEmailBE(request.getEmailBE());
        facture.setNumBetang(request.getNumBetang());
        
        facture.setDevis(devis);
        facture.setClient(client);

        Facture savedFacture = factureRepository.save(facture);
        logger.info("Facture créée avec l'ID: {}", savedFacture.getId());
        
        return FactureResponseDTO.fromEntity(savedFacture);
    }
    
    @Transactional
    public FactureResponseDTO updateFacture(String id, UpdateFactureRequest request) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture non trouvée avec l'ID: " + id));

        // Mettre à jour les champs autorisés
        if (StringUtils.hasText(request.getNumeroSecretariat())) {
            facture.setNumeroSecretariat(request.getNumeroSecretariat());
        }
        facture.setEmailBE(request.getEmailBE()); // Peut être null
        facture.setNumBetang(request.getNumBetang()); // Peut être null

        // if (StringUtils.hasText(request.getStatut())) {
        //     facture.setStatut(request.getStatut());
        // }
        
        facture.setDateUpdateFacture(LocalDate.now());
        Facture updatedFacture = factureRepository.save(facture);
        logger.info("Facture mise à jour: {}", updatedFacture.getId());

        return FactureResponseDTO.fromEntity(updatedFacture);
    }

    @Transactional(readOnly = true)
    public List<FactureResponseDTO> getAllFactures() {
        return factureRepository.findAll().stream()
                .map(FactureResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FactureResponseDTO getFactureById(String id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture non trouvée avec l'ID: " + id));
        return FactureResponseDTO.fromEntity(facture);
    }

    @Transactional
    public void deleteFacture(String id) {
        if (!factureRepository.existsById(id)) {
            throw new ResourceNotFoundException("Facture non trouvée avec l'ID: " + id);
        }
        factureRepository.deleteById(id);
        logger.info("Facture supprimée: {}", id);
    }
    
    @Transactional(readOnly = true)
    public FacturePdfDTO prepareDataForPdf(String id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture non trouvée avec l'ID: " + id));
        return FacturePdfDTO.fromEntity(facture);
    }

    private String generateNextId() {
        LocalDate now = LocalDate.now();
        String prefix = "FA-BE-" + now.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "-";
        Optional<Facture> lastFactureOpt = factureRepository.findTopByIdStartingWithOrderByIdDesc(prefix);
        int nextSequence = 1;
        if (lastFactureOpt.isPresent()) {
            String lastId = lastFactureOpt.get().getId();
            try {
                String lastSequenceStr = lastId.substring(prefix.length());
                nextSequence = Integer.parseInt(lastSequenceStr) + 1;
            } catch (Exception e) {
                logger.error("Impossible de parser le dernier ID de facture: {}", lastId, e);
            }
        }
        return prefix + String.format("%03d", nextSequence);
    }
}