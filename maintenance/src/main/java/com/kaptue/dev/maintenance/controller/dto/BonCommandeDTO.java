package com.kaptue.dev.maintenance.controller.dto;

import java.time.LocalDate;

public class BonCommandeDTO {
    private String id;
    private LocalDate dateCommande;
    private String codeProjet;
    private String codeDevise;
    private String adresseLivraison;
    private String modeExpedition;
    private String modePaiement;
    private String moyenPaiement;
    private String emailBe;
    private String fournisseurNom; // Nom du fournisseur
    private String devisId; // ID du devis

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

    public String getCodeProjet() {
        return this.codeProjet;
    }

    public void setCodeProjet(String codeProjet) {
        this.codeProjet = codeProjet;
    }

    public String getCodeDevise() {
        return this.codeDevise;
    }

    public void setCodeDevise(String codeDevise) {
        this.codeDevise = codeDevise;
    }

    public String getAdresseLivraison() {
        return this.adresseLivraison;
    }

    public void setAdresseLivraison(String adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
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

    public String getEmailBe() {
        return this.emailBe;
    }

    public void setEmailBe(String emailBe) {
        this.emailBe = emailBe;
    }

    public String getFournisseurNom() {
        return this.fournisseurNom;
    }

    public void setFournisseurNom(String fournisseurNom) {
        this.fournisseurNom = fournisseurNom;
    }

    public String getDevisId() {
        return this.devisId;
    }

    public void setDevisId(String devisId) {
        this.devisId = devisId;
    }
    // ...
}


