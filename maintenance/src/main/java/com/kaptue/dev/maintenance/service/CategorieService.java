package com.kaptue.dev.maintenance.service;

import com.kaptue.dev.maintenance.controller.request.CreateCategorieRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateCategorieRequest;
import com.kaptue.dev.maintenance.controller.response.CategorieResponseDTO;
import com.kaptue.dev.maintenance.entity.Categorie;
import com.kaptue.dev.maintenance.exception.DuplicateResourceException;
import com.kaptue.dev.maintenance.exception.ResourceNotFoundException;
import com.kaptue.dev.maintenance.repository.CategorieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategorieService {
    private static final Logger logger = LoggerFactory.getLogger(CategorieService.class);

    @Autowired
    private CategorieRepository categorieRepository;

    @Transactional
    public CategorieResponseDTO createCategorie(CreateCategorieRequest request) {
        if (categorieRepository.existsByNomCategorie(request.getNomCategorie())) {
            throw new DuplicateResourceException("Une catégorie avec le nom '" + request.getNomCategorie() + "' existe déjà.");
        }

        Categorie categorie = new Categorie();
        categorie.setNomCategorie(request.getNomCategorie());

        Categorie savedCategorie = categorieRepository.save(categorie);
        logger.info("Nouvelle catégorie créée avec l'ID: {}", savedCategorie.getId());

        return CategorieResponseDTO.fromEntity(savedCategorie);
    }

    @Transactional
    public CategorieResponseDTO updateCategorie(Long id, UpdateCategorieRequest request) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'ID: " + id));
        
        categorie.setNomCategorie(request.getNomCategorie());
        Categorie updatedCategorie = categorieRepository.save(categorie);
        logger.info("Catégorie ID: {} mise à jour.", updatedCategorie.getId());

        return CategorieResponseDTO.fromEntity(updatedCategorie);
    }

    @Transactional(readOnly = true)
    public List<CategorieResponseDTO> findAll() {
        // Les catégories sont peu nombreuses, la pagination n'est pas forcément nécessaire.
        // On les trie par nom.
        return categorieRepository.findAll(Sort.by("nomCategorie")).stream()
                .map(CategorieResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CategorieResponseDTO findById(Long id) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'ID: " + id));
        return CategorieResponseDTO.fromEntity(categorie);
    }

    @Transactional
    public void deleteCategorie(Long id) {
        if (!categorieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Catégorie non trouvée avec l'ID: " + id);
        }
        // TODO: Vérifier si la catégorie est utilisée par des articles avant de la supprimer.
        // if (articleRepository.existsByCategorieId(id)) {
        //     throw new BadRequestException("Impossible de supprimer cette catégorie car elle est utilisée par des articles.");
        // }
        categorieRepository.deleteById(id);
        logger.info("Catégorie ID: {} supprimée.", id);
    }
}