package com.kaptue.dev.maintenance.controller.response;

import com.kaptue.dev.maintenance.entity.Categorie;

public class CategorieResponseDTO {

    private Long id;
    private String nomCategorie;

    public static CategorieResponseDTO fromEntity(Categorie categorie) {
        if (categorie == null) {
            return null;
        }
        CategorieResponseDTO dto = new CategorieResponseDTO();
        dto.setId(categorie.getId());
        dto.setNomCategorie(categorie.getNomCategorie());
        return dto;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomCategorie() { return nomCategorie; }
    public void setNomCategorie(String nomCategorie) { this.nomCategorie = nomCategorie; }
}