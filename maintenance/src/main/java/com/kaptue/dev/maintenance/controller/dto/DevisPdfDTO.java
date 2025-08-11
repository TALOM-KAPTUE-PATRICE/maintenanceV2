package com.kaptue.dev.maintenance.controller.dto;

import java.time.LocalDate;
import java.util.List;

public class DevisPdfDTO {
    private String id;
    private String description;
    private LocalDate dateCreation;
    private LocalDate dateValidite;
    private String devise;
    private String siteIntervention;
    private String typeTravaux;
    private String contrainte;

    // La liste des catégories contenant les articles
    private List<CategoryPdfDTO> categories;

    // Totaux
    private Double totalHT;
    private Double tva; // (Ex: 19.25%)
    private Double totalTTC;
    private String totalEnLettres;


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateValidite() {
        return this.dateValidite;
    }

    public void setDateValidite(LocalDate dateValidite) {
        this.dateValidite = dateValidite;
    }

    public String getDevise() {
        return this.devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public String getSiteIntervention() {
        return this.siteIntervention;
    }

    public void setSiteIntervention(String siteIntervention) {
        this.siteIntervention = siteIntervention;
    }

    public String getTypeTravaux() {
        return this.typeTravaux;
    }

    public void setTypeTravaux(String typeTravaux) {
        this.typeTravaux = typeTravaux;
    }

    public String getContrainte() {
        return this.contrainte;
    }

    public void setContrainte(String contrainte) {
        this.contrainte = contrainte;
    }

    public List<CategoryPdfDTO> getCategories() {
        return this.categories;
    }

    public void setCategories(List<CategoryPdfDTO> categories) {
        this.categories = categories;
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

    public String getTotalEnLettres() {
        return this.totalEnLettres;
    }

    public void setTotalEnLettres(String totalEnLettres) {
        this.totalEnLettres = totalEnLettres;
    }
    
}