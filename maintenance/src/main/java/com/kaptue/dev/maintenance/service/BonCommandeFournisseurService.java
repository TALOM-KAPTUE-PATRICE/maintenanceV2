package com.kaptue.dev.maintenance.service;

import com.kaptue.dev.maintenance.controller.dto.BonCommandePdfDTO;
import com.kaptue.dev.maintenance.controller.request.CreateBonCommandeRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateBonCommandeRequest;
import com.kaptue.dev.maintenance.controller.response.BonCommandeResponseDTO;
import com.kaptue.dev.maintenance.entity.BonCommandeFournisseur;
import com.kaptue.dev.maintenance.entity.Devis;
import com.kaptue.dev.maintenance.entity.Fournisseur;
import com.kaptue.dev.maintenance.exception.ResourceNotFoundException;
import com.kaptue.dev.maintenance.repository.BonCommandeFournisseurRepository;
import com.kaptue.dev.maintenance.repository.DevisRepository;
import com.kaptue.dev.maintenance.repository.FournisseurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BonCommandeFournisseurService {

    private static final Logger logger = LoggerFactory.getLogger(BonCommandeFournisseurService.class);

    @Autowired
    private BonCommandeFournisseurRepository bonCommandeRepository;
    @Autowired
    private DevisRepository devisRepository;
    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Transactional
    public BonCommandeResponseDTO createBonCommande(CreateBonCommandeRequest request) {
        Devis devis = devisRepository.findById(request.getDevisId())
                .orElseThrow(() -> new ResourceNotFoundException("Devis non trouvé avec l'ID: " + request.getDevisId()));

        Fournisseur fournisseur = fournisseurRepository.findById(request.getFournisseurId())
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé avec l'ID: " + request.getFournisseurId()));

        BonCommandeFournisseur bonCommande = new BonCommandeFournisseur();
        bonCommande.setId(generateNextId()); // Utilisation de la nouvelle méthode sécurisée
        bonCommande.setDateCommande(LocalDate.now());
        bonCommande.setDateUpdateCommande(LocalDate.now());

        // Mapper depuis la requête
        bonCommande.setAdresseLivraison(request.getAdresseLivraison());
        bonCommande.setCodeProjet(request.getCodeProjet());
        bonCommande.setModeExpedition(request.getModeExpedition());
        bonCommande.setModePaiement(request.getModePaiement());
        bonCommande.setMoyenPaiement(request.getMoyenPaiement());
        bonCommande.setCodeDevise(request.getCodeDevise());

        // Assigner les relations
        bonCommande.setDevis(devis);
        bonCommande.setFournisseur(fournisseur);

        BonCommandeFournisseur savedBonCommande = bonCommandeRepository.save(bonCommande);
        logger.info("Bon de commande créé avec l'ID: {}", savedBonCommande.getId());

        return BonCommandeResponseDTO.fromEntity(savedBonCommande);
    }

    @Transactional(readOnly = true)
    public List<BonCommandeResponseDTO> getAllBonCommandes() {
        return bonCommandeRepository.findAll().stream()
                .map(BonCommandeResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BonCommandeResponseDTO getBonCommandeById(String id) {
        BonCommandeFournisseur bc = bonCommandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bon de commande non trouvé avec l'ID: " + id));
        return BonCommandeResponseDTO.fromEntity(bc);
    }

    @Transactional
    public void deleteBonCommande(String id) {
        if (!bonCommandeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bon de commande non trouvé avec l'ID: " + id);
        }
        bonCommandeRepository.deleteById(id);
        logger.info("Bon de commande supprimé: {}", id);
    }

    // Dans BonCommandeService.java
    @Transactional
    public BonCommandeResponseDTO updateBonCommande(String id, UpdateBonCommandeRequest request) {
        BonCommandeFournisseur bonCommande = bonCommandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bon de commande non trouvé avec l'ID: " + id));

        // Mettre à jour les champs
        bonCommande.setAdresseLivraison(request.getAdresseLivraison());
        bonCommande.setCodeProjet(request.getCodeProjet());
        bonCommande.setModeExpedition(request.getModeExpedition());
        bonCommande.setModePaiement(request.getModePaiement());
        bonCommande.setMoyenPaiement(request.getMoyenPaiement());
        bonCommande.setDateUpdateCommande(LocalDate.now());

        BonCommandeFournisseur updatedBonCommande = bonCommandeRepository.save(bonCommande);
        logger.info("Bon de commande mis à jour: {}", updatedBonCommande.getId());

        return BonCommandeResponseDTO.fromEntity(updatedBonCommande);
    }

    @Transactional(readOnly = true)
    public BonCommandePdfDTO prepareDataForPdf(String id) {
        BonCommandeFournisseur bc = bonCommandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bon de commande non trouvé avec l'ID: " + id));
        // La transaction garantit que toutes les données (fournisseur, devis, articles) sont chargées
        return BonCommandePdfDTO.fromEntity(bc);
    }

    /**
     * Génère le prochain ID de manière atomique et sécurisée. Format:
     * BC-BE-YYYY-MM-NNN
     *
     * @return Le nouvel ID.
     */
    private String generateNextId() {
        LocalDate now = LocalDate.now();
        String prefix = "BC-BE-" + now.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "-";

        Optional<BonCommandeFournisseur> lastBonCommandeOpt = bonCommandeRepository.findTopByIdStartingWithOrderByIdDesc(prefix);

        int nextSequence = 1;
        if (lastBonCommandeOpt.isPresent()) {
            String lastId = lastBonCommandeOpt.get().getId();
            try {
                String lastSequenceStr = lastId.substring(prefix.length());
                int lastSequence = Integer.parseInt(lastSequenceStr);
                nextSequence = lastSequence + 1;
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                logger.error("Impossible de parser le dernier ID: {}. Réinitialisation à 1.", lastId, e);
                // En cas d'ID mal formé, on repart à 1 pour ce mois, c'est une sécurité.
            }
        }

        return prefix + String.format("%03d", nextSequence);
    }
}
