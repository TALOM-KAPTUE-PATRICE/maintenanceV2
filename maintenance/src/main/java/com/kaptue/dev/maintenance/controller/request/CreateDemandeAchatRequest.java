package com.kaptue.dev.maintenance.controller.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class CreateDemandeAchatRequest {


    @NotBlank(message = "Le lieu de livraison est requis")
    private String lieuLivraison;

    @NotBlank(message = "Le service demandeur est requis")
    private String serviceDemandeur;

    @NotBlank(message = "L'objet de la demande est requis")
    private String objet;

    @NotNull(message = "L'ID du ticket associé est requis")
    private Long ticketId;

    @NotNull(message = "L'ID du devis associé est requis")
    private String devisId; // L'ID du devis est une String

    // Getters et Setters
  
    public String getLieuLivraison() { return lieuLivraison; }
    public void setLieuLivraison(String lieuLivraison) { this.lieuLivraison = lieuLivraison; }
    public String getServiceDemandeur() { return serviceDemandeur; }
    public void setServiceDemandeur(String serviceDemandeur) { this.serviceDemandeur = serviceDemandeur; }
    public String getObjet() { return objet; }
    public void setObjet(String objet) { this.objet = objet; }
    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }
    public String getDevisId() { return devisId; }
    public void setDevisId(String devisId) { this.devisId = devisId; }
}
