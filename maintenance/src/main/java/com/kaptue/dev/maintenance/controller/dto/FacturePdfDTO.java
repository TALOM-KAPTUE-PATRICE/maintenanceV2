package com.kaptue.dev.maintenance.controller.dto;

import com.kaptue.dev.maintenance.entity.Client;
import com.kaptue.dev.maintenance.entity.Devis;
import com.kaptue.dev.maintenance.entity.Facture;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class FacturePdfDTO {

    // Infos Facture
    private String id;
    private LocalDate dateFacturation;
    private LocalDate dateEcheance; // Calculée (ex: 30 jours après)
    private String numeroSecretariat;

    // Infos Client
    private String clientNom;
    private String clientEmail;
    private String clientNumero;
    
    // Infos Devis & Articles
    private String devisId;
    private String devisDescription;
    private String devise;
    private List<ArticlePdfDTO> articles; // On réutilise le DTO d'article
    private Double totalHT;
    private Double tva;
    private Double totalTTC;
    private String totalTtcEnLettres; // Ajout très professionnel

    public static FacturePdfDTO fromEntity(Facture facture) {
        FacturePdfDTO dto = new FacturePdfDTO();
        Devis devis = facture.getDevis();
        Client client = facture.getClient();

        // Facture
        dto.setId(facture.getId());
        dto.setDateFacturation(facture.getDateFacturation());
        dto.setDateEcheance(facture.getDateFacturation().plusDays(30)); // Échéance à 30 jours
        dto.setNumeroSecretariat(facture.getNumeroSecretariat());
        
        // Client
        if (client != null) {
            dto.setClientNom(client.getNom());
            dto.setClientEmail(client.getEmail());
            dto.setClientNumero(client.getNumero());
        }

        // Devis et Articles
        if (devis != null) {
            dto.setDevisId(devis.getId());
            dto.setDevisDescription(devis.getDescription());
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
            double tvaRate = 0.1925;
            double tvaAmount = totalHt * tvaRate;
            double totalTtc = totalHt + tvaAmount;
            
            dto.setTotalHT(totalHt);
            dto.setTva(tvaAmount);
            dto.setTotalTTC(totalTtc);
            
            // Conversion en lettres (simplifié, une librairie serait mieux pour la prod)
            // dto.setTotalTtcEnLettres(convertirEnLettres(totalTtc));
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

    public LocalDate getDateFacturation() {
        return this.dateFacturation;
    }

    public void setDateFacturation(LocalDate dateFacturation) {
        this.dateFacturation = dateFacturation;
    }

    public LocalDate getDateEcheance() {
        return this.dateEcheance;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
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

    public String getClientEmail() {
        return this.clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientNumero() {
        return this.clientNumero;
    }

    public void setClientNumero(String clientNumero) {
        this.clientNumero = clientNumero;
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

    public String getTotalTtcEnLettres() {
        return this.totalTtcEnLettres;
    }

    public void setTotalTtcEnLettres(String totalTtcEnLettres) {
        this.totalTtcEnLettres = totalTtcEnLettres;
    }

}