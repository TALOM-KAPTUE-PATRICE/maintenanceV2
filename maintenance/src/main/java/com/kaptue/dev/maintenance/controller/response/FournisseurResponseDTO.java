package com.kaptue.dev.maintenance.controller.response;

import com.kaptue.dev.maintenance.entity.Fournisseur;

public class FournisseurResponseDTO {

    private Long id;
    private String nomFourniss;
    private String emailFourniss;
    private String localisation;
    private String numeroFourniss;

    public static FournisseurResponseDTO fromEntity(Fournisseur fournisseur) {
        if (fournisseur == null) {
            return null;
        }
        FournisseurResponseDTO dto = new FournisseurResponseDTO();
        dto.setId(fournisseur.getId());
        dto.setNomFourniss(fournisseur.getNomFourniss());
        dto.setEmailFourniss(fournisseur.getEmailFourniss());
        dto.setLocalisation(fournisseur.getLocalisation());
        dto.setNumeroFourniss(fournisseur.getNumeroFourniss());
        return dto;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomFourniss() { return nomFourniss; }
    public void setNomFourniss(String nomFourniss) { this.nomFourniss = nomFourniss; }
    public String getEmailFourniss() { return emailFourniss; }
    public void setEmailFourniss(String emailFourniss) { this.emailFourniss = emailFourniss; }
    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public String getNumeroFourniss() { return numeroFourniss; }
    public void setNumeroFourniss(String numeroFourniss) { this.numeroFourniss = numeroFourniss; }
}