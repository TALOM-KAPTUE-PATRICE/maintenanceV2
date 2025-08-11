package com.kaptue.dev.maintenance.controller.response;

import com.kaptue.dev.maintenance.entity.Devis;
import com.kaptue.dev.maintenance.entity.enums.DevisStatus;

import java.time.LocalDate;

public class DevisResponseDTO {

    private String id;
    private String description;
    private LocalDate dateCreation;
    private LocalDate dateValidite;
    private String typeTravaux;
    private boolean peinture;
    private int effectif;
    private String devise;
    private String siteIntervention;
    private DevisStatus statut;
    private String client;

    public static DevisResponseDTO fromEntity(Devis devis) {
        if (devis == null) return null;
        DevisResponseDTO dto = new DevisResponseDTO();
        dto.setId(devis.getId());
        dto.setDescription(devis.getDescription());
        dto.setDateCreation(devis.getDateCreation());
        dto.setDateValidite(devis.getDateValidite());
        dto.setTypeTravaux(devis.getTypeTravaux());
        dto.setPeinture(devis.isPeinture());
        dto.setEffectif(devis.getEffectif());
        dto.setDevise(devis.getDevise());
        dto.setSiteIntervention(devis.getSiteIntervention());
        dto.setStatut(devis.getStatut());

        if(devis.getClient() != null){
            dto.setClient(devis.getClient().getEmail());
        }
        if(devis.getArticles() != null){

        }
        return dto;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateValidite() {
        return this.dateValidite;
    }

    public void setDateValidite(LocalDate dateValidite) {
        this.dateValidite = dateValidite;
    }


    public String getClient() {
        return this.client;
    }

    public void setClient(String client) {
        this.client = client;
    }


    public String getTypeTravaux() {
        return this.typeTravaux;
    }

    public void setTypeTravaux(String typeTravaux) {
        this.typeTravaux = typeTravaux;
    }

    public boolean isPeinture() {
        return this.peinture;
    }

    public boolean getPeinture() {
        return this.peinture;
    }

    public void setPeinture(boolean peinture) {
        this.peinture = peinture;
    }

    public int getEffectif() {
        return this.effectif;
    }

    public void setEffectif(int effectif) {
        this.effectif = effectif;
    }

    public String getDevise() {
        return this.devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public String getSiteIntervention() {
        return this.siteIntervention;
    }

    public void setSiteIntervention(String siteIntervention) {
        this.siteIntervention = siteIntervention;
    }

    public DevisStatus getStatut() {
        return this.statut;
    }

    public void setStatut(DevisStatus statut) {
        this.statut = statut;
    }

   
}