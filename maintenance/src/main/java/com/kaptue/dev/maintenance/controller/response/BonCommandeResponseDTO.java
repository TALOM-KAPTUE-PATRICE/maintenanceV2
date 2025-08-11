package com.kaptue.dev.maintenance.controller.response;

import java.time.LocalDate;
import com.kaptue.dev.maintenance.entity.BonCommandeFournisseur;
import com.kaptue.dev.maintenance.entity.enums.BcFournisseur;

public class BonCommandeResponseDTO {

    private String id;
    private LocalDate dateCommande;
    private LocalDate dateUpdateCommande;
    private String adresseLivraison;
    private BcFournisseur statut; 
    private String devisId;
    private String fournisseurNom;
    private String fournisseurEmail;

    public static BonCommandeResponseDTO fromEntity(BonCommandeFournisseur bc) {
        BonCommandeResponseDTO dto = new BonCommandeResponseDTO();
        dto.setId(bc.getId());
        dto.setDateCommande(bc.getDateCommande());
        dto.setDateUpdateCommande(bc.getDateUpdateCommande());
        dto.setAdresseLivraison(bc.getAdresseLivraison());
        dto.setStatut(bc.getStatut());

        if (bc.getDevis() != null) {
            dto.setDevisId(bc.getDevis().getId());
        }
        if (bc.getFournisseur() != null) {
            dto.setFournisseurNom(bc.getFournisseur().getNomFourniss());
            dto.setFournisseurEmail(bc.getFournisseur().getEmailFourniss());
        }
        return dto;
    }

    // Getters et Setters

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDateCommande() {
        return this.dateCommande;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public LocalDate getDateUpdateCommande() {
        return this.dateUpdateCommande;
    }

    public void setDateUpdateCommande(LocalDate dateUpdateCommande) {
        this.dateUpdateCommande = dateUpdateCommande;
    }

    public String getAdresseLivraison() {
        return this.adresseLivraison;
    }

    public void setAdresseLivraison(String adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public BcFournisseur getStatut() {
        return this.statut;
    }


    public void setStatut(BcFournisseur statut) {
        this.statut = statut;
    }



    

    public String getDevisId() {
        return this.devisId;
    }

    public void setDevisId(String devisId) {
        this.devisId = devisId;
    }

    public String getFournisseurNom() {
        return this.fournisseurNom;
    }

    public void setFournisseurNom(String fournisseurNom) {
        this.fournisseurNom = fournisseurNom;
    }

    public String getFournisseurEmail() {
        return this.fournisseurEmail;
    }

    public void setFournisseurEmail(String fournisseurEmail) {
        this.fournisseurEmail = fournisseurEmail;
    }
    

}