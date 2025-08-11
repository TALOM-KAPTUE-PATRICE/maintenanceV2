package com.kaptue.dev.maintenance.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateBonCommandeRequest {

    @NotBlank(message = "L'ID du devis est requis")
    private String devisId;

    @NotNull(message = "L'ID du fournisseur est requis")
    private Long fournisseurId;

    @NotBlank(message = "L'adresse de livraison est requise")
    private String adresseLivraison;

    @NotBlank(message = "Le code requise")
    private String codeDevise;

    // Ajouter d'autres champs que l'utilisateur doit fournir à la création
    private String codeProjet;
    private String modeExpedition;
    private String modePaiement;
    private String moyenPaiement;
    

    // Getters et Setters
    public String getDevisId() { return devisId; }
    public void setDevisId(String devisId) { this.devisId = devisId; }
    public Long getFournisseurId() { return fournisseurId; }
    public void setFournisseurId(Long fournisseurId) { this.fournisseurId = fournisseurId; }
    public String getAdresseLivraison() { return adresseLivraison; }
    public void setAdresseLivraison(String adresseLivraison) { this.adresseLivraison = adresseLivraison; }
    public String getCodeProjet() { return codeProjet; }
    public void setCodeProjet(String codeProjet) { this.codeProjet = codeProjet; }
    public String getModeExpedition() { return modeExpedition; }
    public void setModeExpedition(String modeExpedition) { this.modeExpedition = modeExpedition; }
    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }
    public String getMoyenPaiement() { return moyenPaiement; }
    public void setMoyenPaiement(String moyenPaiement) { this.moyenPaiement = moyenPaiement; }

    public String getCodeDevise() {
        return this.codeDevise;
    }

    public void setCodeDevise(String codeDevise) {
        this.codeDevise = codeDevise;
    }






}