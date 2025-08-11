package com.kaptue.dev.maintenance.controller.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.kaptue.dev.maintenance.entity.BonCommandeFournisseur;
import com.kaptue.dev.maintenance.entity.Devis;
import com.kaptue.dev.maintenance.entity.Fournisseur;

public class BonCommandePdfDTO {

    // Infos Commande
    private String id;
    private LocalDate dateCommande;
    private String adresseLivraison;
    private String codeProjet;
    private String modeExpedition;
    private String modePaiement;
    private String moyenPaiement;

    // Infos Fournisseur
    private String fournisseurNom;
    private String fournisseurLocalisation;
    private String fournisseurEmail;
    private String fournisseurNumero;

    // Infos Devis & Articles
    private String devisId;
    private String devise;
    private List<ArticlePdfDTO> articles; // On réutilise le DTO d'article
    private Double totalHT;
    private Double tva; // Exemple, 19.25%
    private Double totalTTC;

    public static BonCommandePdfDTO fromEntity(BonCommandeFournisseur bc) {
        BonCommandePdfDTO dto = new BonCommandePdfDTO();
        Devis devis = bc.getDevis();
        Fournisseur fournisseur = bc.getFournisseur();

        // Commande
        dto.setId(bc.getId());
        dto.setDateCommande(bc.getDateCommande());
        dto.setAdresseLivraison(bc.getAdresseLivraison());
        dto.setCodeProjet(bc.getCodeProjet());
        dto.setModeExpedition(bc.getModeExpedition());
        dto.setModePaiement(bc.getModePaiement());
        dto.setMoyenPaiement(bc.getMoyenPaiement());

        // Fournisseur
        if (fournisseur != null) {
            dto.setFournisseurNom(fournisseur.getNomFourniss());
            dto.setFournisseurLocalisation(fournisseur.getLocalisation());
            dto.setFournisseurEmail(fournisseur.getEmailFourniss());
            dto.setFournisseurNumero(fournisseur.getNumeroFourniss());
        }

        // Devis et Articles
        if (devis != null) {
            dto.setDevisId(devis.getId());
            dto.setDevise(devis.getDevise());

            List<ArticlePdfDTO> articleDtos = devis.getArticles().stream().map(article -> {
                ArticlePdfDTO articleDto = new ArticlePdfDTO();
                articleDto.setDesignation(article.getDesignation());
                articleDto.setQuantite(article.getQuantite());
                articleDto.setPrixUnitaire(article.getPrixUnitaire());
                articleDto.setPrixTotal(article.getQuantite() * article.getPrixUnitaire());
                return articleDto;
            }).collect(Collectors.toList());
            dto.setArticles(articleDtos);

            // Calculs
            double totalHt = articleDtos.stream().mapToDouble(ArticlePdfDTO::getPrixTotal).sum();
            double tvaRate = 0.1925; // Taux de TVA à 19.25%
            double tvaAmount = totalHt * tvaRate;
            
            dto.setTotalHT(totalHt);
            dto.setTva(tvaAmount);
            dto.setTotalTTC(totalHt + tvaAmount);
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

    public String getFournisseurNom() {
        return this.fournisseurNom;
    }

    public void setFournisseurNom(String fournisseurNom) {
        this.fournisseurNom = fournisseurNom;
    }

    public String getFournisseurLocalisation() {
        return this.fournisseurLocalisation;
    }

    public void setFournisseurLocalisation(String fournisseurLocalisation) {
        this.fournisseurLocalisation = fournisseurLocalisation;
    }

    public String getFournisseurEmail() {
        return this.fournisseurEmail;
    }

    public void setFournisseurEmail(String fournisseurEmail) {
        this.fournisseurEmail = fournisseurEmail;
    }

    public String getFournisseurNumero() {
        return this.fournisseurNumero;
    }

    public void setFournisseurNumero(String fournisseurNumero) {
        this.fournisseurNumero = fournisseurNumero;
    }

    public String getDevisId() {
        return this.devisId;
    }

    public void setDevisId(String devisId) {
        this.devisId = devisId;
    }

    public String getDevise() {
        return this.devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public List<ArticlePdfDTO> getArticles() {
        return this.articles;
    }

    public void setArticles(List<ArticlePdfDTO> articles) {
        this.articles = articles;
    }

    public Double getTotalHT() {
        return this.totalHT;
    }

    public void setTotalHT(Double totalHT) {
        this.totalHT = totalHT;
    }

    public Double getTva() {
        return this.tva;
    }

    public void setTva(Double tva) {
        this.tva = tva;
    }

    public Double getTotalTTC() {
        return this.totalTTC;
    }

    public void setTotalTTC(Double totalTTC) {
        this.totalTTC = totalTTC;
    }
    
}