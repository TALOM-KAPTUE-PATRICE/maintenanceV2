// FactureDTO.java
package com.kaptue.dev.maintenance.controller.dto;

import java.time.LocalDate;

public class FactureDTO {
    private String id;
    private double montant;
    private String numeroSecretariat;
    private String emailBE;
    private String numBetang;
    private LocalDate dateFacturation;
    private Long clientId;
    private String clientEmail;
    private String clientNom; // Ajout du nom du client
    private String devisId;
    private String devisDescription;
    

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

    public String getNumeroSecretariat() {
        return this.numeroSecretariat;
    }

    public void setNumeroSecretariat(String numeroSecretariat) {
        this.numeroSecretariat = numeroSecretariat;
    }

    public String getEmailBE() {
        return this.emailBE;
    }

    public void setEmailBE(String emailBE) {
        this.emailBE = emailBE;
    }

    public String getNumBetang() {
        return this.numBetang;
    }

    public void setNumBetang(String numBetang) {
        this.numBetang = numBetang;
    }

    public LocalDate getDateFacturation() {
        return this.dateFacturation;
    }

    public void setDateFacturation(LocalDate dateFacturation) {
        this.dateFacturation = dateFacturation;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientEmail() {
        return this.clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
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

    public String getDevisDescription() {
        return this.devisDescription;
    }

    public void setDevisDescription(String devisDescription) {
        this.devisDescription = devisDescription;
    }
    

}