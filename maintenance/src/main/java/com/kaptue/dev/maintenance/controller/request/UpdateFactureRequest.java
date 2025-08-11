package com.kaptue.dev.maintenance.controller.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateFactureRequest {

    @NotBlank(message = "Le numéro de secrétariat est requis")
    private String numeroSecretariat;
    
    private String emailBE;
    private String numBetang;
    private String statut; // Pour un futur workflow de paiement

    // Getters et Setters
    public String getNumeroSecretariat() { return numeroSecretariat; }
    public void setNumeroSecretariat(String numeroSecretariat) { this.numeroSecretariat = numeroSecretariat; }
    public String getEmailBE() { return emailBE; }
    public void setEmailBE(String emailBE) { this.emailBE = emailBE; }
    public String getNumBetang() { return numBetang; }
    public void setNumBetang(String numBetang) { this.numBetang = numBetang; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}