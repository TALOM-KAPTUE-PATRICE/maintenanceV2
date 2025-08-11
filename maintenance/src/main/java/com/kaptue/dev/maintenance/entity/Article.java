package com.kaptue.dev.maintenance.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name = "articles" )
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "designation")
    private String designation;

    @Column(name = "quantite")
    private Integer quantite;

    @Column(name = "datecreationArt")
    private LocalDate datecreationArt;
    
    @Column(name = "dateUpdateArt")
    private LocalDate dateUpdateArt;

    @Column(name = "prixUnitaire")
    private Double prixUnitaire;

    @ManyToOne
    @JoinColumn(name = "id_categorie")
    private Categorie categorie;

    @ManyToMany(mappedBy = "articles")
    @JsonIgnore 
    private Set<Devis> devis = new HashSet<>();

    public Article() {
        this.datecreationArt= LocalDate.now();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return this.designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public LocalDate getDateUpdateArt() {
        return this.dateUpdateArt;
    }

    public void setDateUpdateArt(LocalDate dateUpdateArt) {
        this.dateUpdateArt = dateUpdateArt;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public LocalDate getDatecreationArt() {
        return this.datecreationArt;
    }

    public void setDatecreationArt(LocalDate datecreationArt) {
        this.datecreationArt = datecreationArt;
    }

    public Double getPrixUnitaire() {
        return this.prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Categorie getCategorie() {
        return this.categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Set<Devis> getDevis() {
        return this.devis;
    }

    public void setDevis(Set<Devis> devis) {
        this.devis = devis;
    }
    



    
}
