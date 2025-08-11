package com.kaptue.dev.maintenance.controller.response;


import com.kaptue.dev.maintenance.entity.DemandeAchat;
import com.kaptue.dev.maintenance.entity.enums.DemandeAchatStatus;
import java.time.LocalDate;

public class DemandeAchatResponseDTO {
    private Long id;
    private String nomInitiateur;
    private LocalDate dateCreationDa;
    private String lieuLivraison;
    private String serviceDemandeur;
    private String objet;
    private DemandeAchatStatus statut;
    private Long ticketId;
    private String ticketTitre;
    private String devisId;

    // Constructeur vide nécessaire pour certains frameworks
    public DemandeAchatResponseDTO() {}

    public static DemandeAchatResponseDTO fromEntity(DemandeAchat da) {
        if (da == null) return null;
        
        DemandeAchatResponseDTO dto = new DemandeAchatResponseDTO();
        dto.setId(da.getId());
        dto.setNomInitiateur(da.getNomInitiateur());
        dto.setDateCreationDa(da.getDateCreationDa());
        dto.setLieuLivraison(da.getLieuLivraison());
        dto.setServiceDemandeur(da.getServiceDemandeur());
        dto.setObjet(da.getObjet());
        dto.setStatut(da.getStatut());
        
        if (da.getTicket() != null) {
            dto.setTicketId(da.getTicket().getId());
            dto.setTicketTitre(da.getTicket().getTitre());
        }
        if (da.getDevis() != null) {
            dto.setDevisId(da.getDevis().getId());
        }
        return dto;
    }
    
    // Générez tous les getters et setters

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomInitiateur() {
        return this.nomInitiateur;
    }

    public void setNomInitiateur(String nomInitiateur) {
        this.nomInitiateur = nomInitiateur;
    }

    public LocalDate getDateCreationDa() {
        return this.dateCreationDa;
    }

    public void setDateCreationDa(LocalDate dateCreationDa) {
        this.dateCreationDa = dateCreationDa;
    }

    public String getLieuLivraison() {
        return this.lieuLivraison;
    }

    public void setLieuLivraison(String lieuLivraison) {
        this.lieuLivraison = lieuLivraison;
    }

    public String getServiceDemandeur() {
        return this.serviceDemandeur;
    }

    public void setServiceDemandeur(String serviceDemandeur) {
        this.serviceDemandeur = serviceDemandeur;
    }

    public String getObjet() {
        return this.objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public DemandeAchatStatus getStatut() {
        return this.statut;
    }

    public void setStatut(DemandeAchatStatus statut) {
        this.statut = statut;
    }

    public Long getTicketId() {
        return this.ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketTitre() {
        return this.ticketTitre;
    }

    public void setTicketTitre(String ticketTitre) {
        this.ticketTitre = ticketTitre;
    }

    public String getDevisId() {
        return this.devisId;
    }

    public void setDevisId(String devisId) {
        this.devisId = devisId;
    }


}