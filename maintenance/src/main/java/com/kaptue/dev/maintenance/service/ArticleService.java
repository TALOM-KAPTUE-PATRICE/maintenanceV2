package com.kaptue.dev.maintenance.service;

import com.kaptue.dev.maintenance.controller.request.CreateArticleRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateArticleRequest;
import com.kaptue.dev.maintenance.controller.response.ArticleResponseDTO;
import com.kaptue.dev.maintenance.entity.Article;
import com.kaptue.dev.maintenance.entity.Categorie;
import com.kaptue.dev.maintenance.exception.DuplicateResourceException;
import com.kaptue.dev.maintenance.exception.ResourceNotFoundException;
import com.kaptue.dev.maintenance.repository.ArticleRepository;
import com.kaptue.dev.maintenance.repository.CategorieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategorieRepository categorieRepository; // Pour lier la catégorie

    @Transactional
    public ArticleResponseDTO createArticle(CreateArticleRequest request) {
        if (articleRepository.existsByDesignation(request.getDesignation())) {
            throw new DuplicateResourceException("Un article avec la désignation '" + request.getDesignation() + "' existe déjà.");
        }

        Categorie categorie = categorieRepository.findById(request.getCategorieId())
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'ID: " + request.getCategorieId()));

        Article article = new Article();
        article.setDesignation(request.getDesignation());
        article.setQuantite(request.getQuantite());
        article.setPrixUnitaire(request.getPrixUnitaire());
        article.setCategorie(categorie);
        article.setDatecreationArt(LocalDate.now());
        article.setDateUpdateArt(LocalDate.now());
        Article savedArticle = articleRepository.save(article);
        logger.info("Nouvel article créé avec l'ID: {}", savedArticle.getId());

        return ArticleResponseDTO.fromEntity(savedArticle);
    }

    @Transactional
    public ArticleResponseDTO updateArticle(Long id, UpdateArticleRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article non trouvé avec l'ID: " + id));
        
        Categorie categorie = categorieRepository.findById(request.getCategorieId())
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'ID: " + request.getCategorieId()));

        article.setDesignation(request.getDesignation());
        article.setQuantite(request.getQuantite());
        article.setPrixUnitaire(request.getPrixUnitaire());
        article.setCategorie(categorie);
        article.setDateUpdateArt(LocalDate.now());

        Article updatedArticle = articleRepository.save(article);
        logger.info("Article ID: {} mis à jour.", updatedArticle.getId());

        return ArticleResponseDTO.fromEntity(updatedArticle);
    }

    @Transactional(readOnly = true)
    public Page<ArticleResponseDTO> findAllPaginated(Pageable pageable) {
        return articleRepository.findAll(pageable).map(ArticleResponseDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public ArticleResponseDTO findById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article non trouvé avec l'ID: " + id));
        return ArticleResponseDTO.fromEntity(article);
    }

    @Transactional
    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Article non trouvé avec l'ID: " + id);
        }
        // TODO: Vérifier si l'article est utilisé dans des devis avant de le supprimer.
        // Cela peut être complexe à cause de la relation ManyToMany. Une suppression "logique" (soft delete)
        // serait peut-être une meilleure approche ici.
        articleRepository.deleteById(id);
        logger.info("Article ID: {} supprimé.", id);
    }
}