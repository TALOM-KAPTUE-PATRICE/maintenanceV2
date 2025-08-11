package com.kaptue.dev.maintenance.controller.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateBonCommandeRequest {
    
    @NotBlank(message = "L'adresse de livraison est requise")
    private String adresseLivraison;
    
    private String codeProjet;
    private String modeExpedition;
    private String modePaiement;
    private String moyenPaiement;

    // Getters et Setters...


    public String getAdresseLivraison() {
        return this.adresseLivraison;
    }

    public void setAdresseLivraison(String adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public String getCodeProjet() {
        return this.codeProjet;
    }

    public void setCodeProjet(String codeProjet) {
        this.codeProjet = codeProjet;
    }

    public String getModeExpedition() {
        return this.modeExpedition;
    }

    public void setModeExpedition(String modeExpedition) {
        this.modeExpedition = modeExpedition;
    }

    public String getModePaiement() {
        return this.modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public String getMoyenPaiement() {
        return this.moyenPaiement;
    }

    public void setMoyenPaiement(String moyenPaiement) {
        this.moyenPaiement = moyenPaiement;
    }

}