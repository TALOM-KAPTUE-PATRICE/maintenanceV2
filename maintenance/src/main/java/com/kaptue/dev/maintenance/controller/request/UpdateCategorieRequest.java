package com.kaptue.dev.maintenance.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateCategorieRequest {

    @NotBlank(message = "Le nom de la catégorie ne peut pas être vide.")
    @Size(max = 255, message = "Le nom de la catégorie ne doit pas dépasser 255 caractères.")
    private String nomCategorie;

    // Getters et Setters
    public String getNomCategorie() { return nomCategorie; }
    public void setNomCategorie(String nomCategorie) { this.nomCategorie = nomCategorie; }
}