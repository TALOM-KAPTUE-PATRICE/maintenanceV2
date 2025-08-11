package com.kaptue.dev.maintenance.controller.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class CreateDevisRequest {

    @NotBlank(message = "La description ne peut pas être vide")
    private String description;

    @NotNull(message = "La date de validité est requise")
    @Future(message = "La date de validité doit être dans le futur")
    private LocalDate dateValidite;

    private String typeTravaux;
    private String contrainte;
    private boolean peinture = false;

    @Min(value = 1, message = "L'effectif doit être d'au moins 1")
    private int effectif;

    private String livraison;

    @NotBlank(message = "La devise est requise")
    private String devise;

    @NotNull(message = "L'ID du client est requis pour le devis.")
    private Long clientId;

    private String siteIntervention;


    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateValidite() {
        return this.dateValidite;
    }

    public void setDateValidite(LocalDate dateValidite) {
        this.dateValidite = dateValidite;
    }

    public String getTypeTravaux() {
        return this.typeTravaux;
    }

    public void setTypeTravaux(String typeTravaux) {
        this.typeTravaux = typeTravaux;
    }

    public String getContrainte() {
        return this.contrainte;
    }

    public void setContrainte(String contrainte) {
        this.contrainte = contrainte;
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

    public String getLivraison() {
        return this.livraison;
    }

    public void setLivraison(String livraison) {
        this.livraison = livraison;
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

    public Long getClientId() {
        return this.clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    

}