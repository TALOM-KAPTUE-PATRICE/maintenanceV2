package com.kaptue.dev.maintenance.controller.dto;
import com.kaptue.dev.maintenance.entity.DemandeAchat;
import com.kaptue.dev.maintenance.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO transportant TOUTES les données formatées pour le template PDF de la Demande d'Achat.
 */
public class DemandeAchatPdfDTO {

    // Infos Demande Achat
    private Long id;
    private String nomInitiateur;
    private String posteInitiateur; // Ajout utile
    private LocalDate dateCreation;
    private String lieuLivraison;
    private String serviceDemandeur;
    private String objet;

    // Infos Ticket lié
    private Long ticketId;
    private String ticketTitre;

    // Infos Devis lié
    private String devisId;
    private String devise;
    private List<ArticlePdfDTO> articles;
    private Double totalHT; // Calculé

    public static DemandeAchatPdfDTO fromEntity(DemandeAchat da, User initiateur) {
        DemandeAchatPdfDTO dto = new DemandeAchatPdfDTO();

        // Demande Achat
        dto.setId(da.getId());
        dto.setNomInitiateur(initiateur.getNom());
        dto.setPosteInitiateur(initiateur.getPoste());
        dto.setDateCreation(da.getDateCreationDa());
        dto.setLieuLivraison(da.getLieuLivraison());
        dto.setServiceDemandeur(da.getServiceDemandeur());
        dto.setObjet(da.getObjet());

        // Ticket
        if (da.getTicket() != null) {
            dto.setTicketId(da.getTicket().getId());
            dto.setTicketTitre(da.getTicket().getTitre());
        }

        // Devis et Articles
        if (da.getDevis() != null) {
            dto.setDevisId(da.getDevis().getId());
            dto.setDevise(da.getDevis().getDevise());

            List<ArticlePdfDTO> articleDtos = da.getDevis().getArticles().stream().map(article -> {
                ArticlePdfDTO articleDto = new ArticlePdfDTO();
                articleDto.setDesignation(article.getDesignation());
                articleDto.setQuantite(article.getQuantite());
                articleDto.setPrixUnitaire(article.getPrixUnitaire());
                // Calculer le prix total pour cet article
                articleDto.setPrixTotal(article.getQuantite() * article.getPrixUnitaire());
                return articleDto;
            }).collect(Collectors.toList());
            dto.setArticles(articleDtos);

            // Calculer le total général
            double total = articleDtos.stream().mapToDouble(ArticlePdfDTO::getPrixTotal).sum();
            dto.setTotalHT(total);
        }

        return dto;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomInitiateur() {
        return this.nomInitiateur;
    }

    public void setNomInitiateur(String nomInitiateur) {
        this.nomInitiateur = nomInitiateur;
    }

    public String getPosteInitiateur() {
        return this.posteInitiateur;
    }

    public void setPosteInitiateur(String posteInitiateur) {
        this.posteInitiateur = posteInitiateur;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getLieuLivraison() {
        return this.lieuLivraison;
    }

    public void setLieuLivraison(String lieuLivraison) {
        this.lieuLivraison = lieuLivraison;
    }

    public String getServiceDemandeur() {
        return this.serviceDemandeur;
    }

    public void setServiceDemandeur(String serviceDemandeur) {
        this.serviceDemandeur = serviceDemandeur;
    }

    public String getObjet() {
        return this.objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public Long getTicketId() {
        return this.ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketTitre() {
        return this.ticketTitre;
    }

    public void setTicketTitre(String ticketTitre) {
        this.ticketTitre = ticketTitre;
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

    // ... Générez tous les Getters et Setters ...
}