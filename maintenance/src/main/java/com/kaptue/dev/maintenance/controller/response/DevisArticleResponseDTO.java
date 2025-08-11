package com.kaptue.dev.maintenance.controller.response;

import com.kaptue.dev.maintenance.entity.Article;

public class DevisArticleResponseDTO {
    private Long id;
    private String designation;
    private Integer quantite;
    private Double prixUnitaire;
    private String nomCategorie;

    public static DevisArticleResponseDTO fromEntity(Article article) {
        if (article == null) return null;
        DevisArticleResponseDTO dto = new DevisArticleResponseDTO();
        dto.setId(article.getId());
        dto.setDesignation(article.getDesignation());
        dto.setQuantite(article.getQuantite());
        dto.setPrixUnitaire(article.getPrixUnitaire());
        if (article.getCategorie() != null) {
            dto.setNomCategorie(article.getCategorie().getNomCategorie());
        }
        return dto;
    }

    // Getters et Setters pour tous les champs...

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

    public String getNomCategorie() {
        return this.nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

}