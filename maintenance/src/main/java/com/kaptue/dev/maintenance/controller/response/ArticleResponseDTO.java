package com.kaptue.dev.maintenance.controller.response;

import com.kaptue.dev.maintenance.entity.Article;
import java.time.LocalDate;

public class ArticleResponseDTO {

    private Long id;
    private String designation;
    private Integer quantite;
    private Double prixUnitaire;
    private LocalDate datecreationArt;
    private LocalDate dateUpdateArt;
    private Long categorieId;
    private String categorieNom;

    public static ArticleResponseDTO fromEntity(Article article) {
        if (article == null) {
            return null;
        }
        ArticleResponseDTO dto = new ArticleResponseDTO();
        dto.setId(article.getId());
        dto.setDesignation(article.getDesignation());
        dto.setQuantite(article.getQuantite());
        dto.setPrixUnitaire(article.getPrixUnitaire());
        dto.setDatecreationArt(article.getDatecreationArt());
        dto.setDateUpdateArt(article.getDateUpdateArt());

        if (article.getCategorie() != null) {
            dto.setCategorieId(article.getCategorie().getId());
            dto.setCategorieNom(article.getCategorie().getNomCategorie());
        }
        return dto;
    }

    // Getters et Setters...

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return this.designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Double getPrixUnitaire() {
        return this.prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public LocalDate getDatecreationArt() {
        return this.datecreationArt;
    }

    public void setDatecreationArt(LocalDate datecreationArt) {
        this.datecreationArt = datecreationArt;
    }

    public LocalDate getDateUpdateArt() {
        return this.dateUpdateArt;
    }

    public void setDateUpdateArt(LocalDate dateUpdateArt) {
        this.dateUpdateArt = dateUpdateArt;
    }

    public Long getCategorieId() {
        return this.categorieId;
    }

    public void setCategorieId(Long categorieId) {
        this.categorieId = categorieId;
    }

    public String getCategorieNom() {
        return this.categorieNom;
    }

    public void setCategorieNom(String categorieNom) {
        this.categorieNom = categorieNom;
    }


}