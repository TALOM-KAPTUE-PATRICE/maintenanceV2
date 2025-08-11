package com.kaptue.dev.maintenance.service;

import com.kaptue.dev.maintenance.controller.request.CreateFournisseurRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateFournisseurRequest;
import com.kaptue.dev.maintenance.controller.response.FournisseurResponseDTO;
import com.kaptue.dev.maintenance.entity.Fournisseur;
import com.kaptue.dev.maintenance.exception.DuplicateResourceException;
import com.kaptue.dev.maintenance.exception.ResourceNotFoundException;
import com.kaptue.dev.maintenance.repository.FournisseurRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FournisseurService {
    private static final Logger logger = LoggerFactory.getLogger(FournisseurService.class);

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Transactional
    public FournisseurResponseDTO createFournisseur(CreateFournisseurRequest request) {
        if (fournisseurRepository.existsByEmailFourniss(request.getEmailFourniss())) {
            throw new DuplicateResourceException("Un fournisseur avec l'email '" + request.getEmailFourniss() + "' existe déjà.");
        }

        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setNomFourniss(request.getNomFourniss());
        fournisseur.setEmailFourniss(request.getEmailFourniss());
        fournisseur.setLocalisation(request.getLocalisation());
        fournisseur.setNumeroFourniss(request.getNumeroFourniss());

        Fournisseur savedFournisseur = fournisseurRepository.save(fournisseur);
        logger.info("Nouveau fournisseur créé avec l'ID: {}", savedFournisseur.getId());

        return FournisseurResponseDTO.fromEntity(savedFournisseur);
    }

    @Transactional
    public FournisseurResponseDTO updateFournisseur(Long id, UpdateFournisseurRequest request) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé avec l'ID: " + id));

        fournisseurRepository.findByEmailFourniss(request.getEmailFourniss()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicateResourceException("L'email '" + request.getEmailFourniss() + "' est déjà utilisé par un autre fournisseur.");
            }
        });

        fournisseur.setNomFourniss(request.getNomFourniss());
        fournisseur.setEmailFourniss(request.getEmailFourniss());
        fournisseur.setLocalisation(request.getLocalisation());
        fournisseur.setNumeroFourniss(request.getNumeroFourniss());

        Fournisseur updatedFournisseur = fournisseurRepository.save(fournisseur);
        logger.info("Fournisseur ID: {} mis à jour.", updatedFournisseur.getId());

        return FournisseurResponseDTO.fromEntity(updatedFournisseur);
    }

    @Transactional(readOnly = true)
    public Page<FournisseurResponseDTO> findAllPaginated(Pageable pageable) {
        return fournisseurRepository.findAll(pageable)
                .map(FournisseurResponseDTO::fromEntity);
    }

        /**
     * NOUVELLE MÉTHODE : Retourne la liste complète de tous les fournisseurs.
     * C'est cette méthode que nous utiliserons pour les listes déroulantes.
     * @return Une liste de FournisseurResponseDTO.
     */
    @Transactional(readOnly = true)
    public List<FournisseurResponseDTO> findAll() {
        return fournisseurRepository.findAll().stream()
                .map(FournisseurResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public FournisseurResponseDTO findById(Long id) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé avec l'ID: " + id));
        return FournisseurResponseDTO.fromEntity(fournisseur);
    }

    @Transactional
    public void deleteFournisseur(Long id) {
        if (!fournisseurRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fournisseur non trouvé avec l'ID: " + id);
        }
        // TODO: Vérifier si le fournisseur est lié à des bons de commande avant de supprimer.
        // if (bonCommandeRepository.existsByFournisseurId(id)) {
        //     throw new BadRequestException("Impossible de supprimer ce fournisseur car il est lié à des bons de commande.");
        // }
        fournisseurRepository.deleteById(id);
        logger.info("Fournisseur ID: {} supprimé.", id);
    }
}