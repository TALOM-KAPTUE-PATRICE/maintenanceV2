package com.kaptue.dev.maintenance.controller.dto;

import java.util.List;

public class CategoryPdfDTO {
    private String nomCategorie;
    private List<ArticlePdfDTO> articles;
    private Double totalCategorie; // Total par catégorie, un plus !


    public String getNomCategorie() {
        return this.nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public List<ArticlePdfDTO> getArticles() {
        return this.articles;
    }

    public void setArticles(List<ArticlePdfDTO> articles) {
        this.articles = articles;
    }

    public Double getTotalCategorie() {
        return this.totalCategorie;
    }

    public void setTotalCategorie(Double totalCategorie) {
        this.totalCategorie = totalCategorie;
    }
    
}