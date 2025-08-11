package com.kaptue.dev.maintenance.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateFactureRequest {

    @NotBlank(message = "L'ID du devis est requis")
    private String devisId;

    @NotNull(message = "L'ID du client est requis")
    private Long clientId;

    @NotBlank(message = "Le numéro de secrétariat est requis")
    private String numeroSecretariat;
    
    private String emailBE; // Email du Bureau d'Études
    private String numBetang; // Numéro interne Betang

    // Getters et Setters
    public String getDevisId() { return devisId; }
    public void setDevisId(String devisId) { this.devisId = devisId; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public String getNumeroSecretariat() { return numeroSecretariat; }
    public void setNumeroSecretariat(String numeroSecretariat) { this.numeroSecretariat = numeroSecretariat; }
    public String getEmailBE() { return emailBE; }
    public void setEmailBE(String emailBE) { this.emailBE = emailBE; }
    public String getNumBetang() { return numBetang; }
    public void setNumBetang(String numBetang) { this.numBetang = numBetang; }
}