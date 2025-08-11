package com.kaptue.dev.maintenance.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class UpdateArticleRequest {

    @NotBlank(message = "La désignation de l'article ne peut pas être vide.")
    @Size(max = 255)
    private String designation;

    @NotNull(message = "La quantité ne peut pas être nulle.")
    @PositiveOrZero(message = "La quantité doit être positive ou nulle.")
    private Integer quantite;

    @NotNull(message = "Le prix unitaire ne peut pas être nul.")
    @Min(value = 0, message = "Le prix unitaire ne peut pas être négatif.")
    private Double prixUnitaire;

    @NotNull(message = "L'ID de la catégorie est requis.")
    private Long categorieId;

    // Getters et Setters
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
    public Double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(Double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public Long getCategorieId() { return categorieId; }
    public void setCategorieId(Long categorieId) { this.categorieId = categorieId; }
}