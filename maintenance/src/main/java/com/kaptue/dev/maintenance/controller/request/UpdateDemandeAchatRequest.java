package com.kaptue.dev.maintenance.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateDemandeAchatRequest {

    @NotBlank(message = "Le lieu de livraison est requis")
    @Size(max = 255)
    private String lieuLivraison;

    @NotBlank(message = "Le service demandeur est requis")
    @Size(max = 255)
    private String serviceDemandeur;

    @NotBlank(message = "L'objet de la demande est requis")
    private String objet;

    // Getters et Setters
    public String getLieuLivraison() { return lieuLivraison; }
    public void setLieuLivraison(String lieuLivraison) { this.lieuLivraison = lieuLivraison; }
    public String getServiceDemandeur() { return serviceDemandeur; }
    public void setServiceDemandeur(String serviceDemandeur) { this.serviceDemandeur = serviceDemandeur; }
    public String getObjet() { return objet; }
    public void setObjet(String objet) { this.objet = objet; }
}