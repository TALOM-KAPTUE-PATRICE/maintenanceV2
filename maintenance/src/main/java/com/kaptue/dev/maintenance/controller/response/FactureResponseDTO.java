package com.kaptue.dev.maintenance.controller.response;

import com.kaptue.dev.maintenance.entity.Facture;
import com.kaptue.dev.maintenance.entity.enums.FactureStatus;

import java.time.LocalDate;

public class FactureResponseDTO {

    private String id;
    private double montant;
    private LocalDate dateFacturation;
    private LocalDate dateUpdateFacture;
    private String numeroSecretariat;
    private String clientNom;
    private String devisId;
    private FactureStatus statut;

    public static FactureResponseDTO fromEntity(Facture facture) {
        FactureResponseDTO dto = new FactureResponseDTO();
        dto.setId(facture.getId());
        dto.setMontant(facture.getMontant());
        dto.setDateFacturation(facture.getDateFacturation());
        dto.setDateUpdateFacture(facture.getDateUpdateFacture());
        dto.setNumeroSecretariat(facture.getNumeroSecretariat());
        dto.setStatut(facture.getStatut());

        if (facture.getClient() != null) {
            dto.setClientNom(facture.getClient().getNom());
        }
        if (facture.getDevis() != null) {
            dto.setDevisId(facture.getDevis().getId());
        }
        return dto;
    }

    // Getters et Setters...

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMontant() {
        return this.montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDate getDateFacturation() {
        return this.dateFacturation;
    }

    public void setDateFacturation(LocalDate dateFacturation) {
        this.dateFacturation = dateFacturation;
    }

    public LocalDate getDateUpdateFacture() {
        return this.dateUpdateFacture;
    }

    public void setDateUpdateFacture(LocalDate dateUpdateFacture) {
        this.dateUpdateFacture = dateUpdateFacture;
    }

    public String getNumeroSecretariat() {
        return this.numeroSecretariat;
    }

    public void setNumeroSecretariat(String numeroSecretariat) {
        this.numeroSecretariat = numeroSecretariat;
    }

    public String getClientNom() {
        return this.clientNom;
    }

    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }

    public String getDevisId() {
        return this.devisId;
    }

    public void setDevisId(String devisId) {
        this.devisId = devisId;
    }


    public FactureStatus getStatut() {
        return this.statut;
    }

    public void setStatut(FactureStatus statut) {
        this.statut = statut;
    }

}