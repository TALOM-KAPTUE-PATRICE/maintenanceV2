package com.kaptue.dev.maintenance.controller.dto;

// DTO simple pour représenter un article dans le PDF
public class ArticlePdfDTO {
    private String designation;
    private Integer quantite;
    private Double prixUnitaire;
    private Double prixTotal; // Champ calculé, très utile pour le template

    // Getters et Setters
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
    public Double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(Double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public Double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(Double prixTotal) { this.prixTotal = prixTotal; }
}