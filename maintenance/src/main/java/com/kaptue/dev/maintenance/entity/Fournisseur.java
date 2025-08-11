package com.kaptue.dev.maintenance.entity;
import java.util.List;

import jakarta.persistence.*;
@Entity
@Table(name = "fournisseur")
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    
    @Column(name = "nomFourniss")
    private String nomFourniss;

    @Column(name = "emailFourniss")
    private String emailFourniss;

    @Column(name = "localisationFourniss")
    private String Localisation;

    @Column(name = "numeroFourniss")
    private String numeroFourniss;

    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.ALL)
    private List<BonCommandeFournisseur> BonCommandes;

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getNomFourniss() {
        return this.nomFourniss;
    }

    public void setNomFourniss(String nomFourniss) {
        this.nomFourniss = nomFourniss;
    }

    public String getEmailFourniss() {
        return this.emailFourniss;
    }

    public void setEmailFourniss(String emailFourniss) {
        this.emailFourniss = emailFourniss;
    }

    public String getLocalisation() {
        return this.Localisation;
    }

    public void setLocalisation(String Localisation) {
        this.Localisation = Localisation;
    }

    public String getNumeroFourniss() {
        return this.numeroFourniss;
    }

    public void setNumeroFourniss(String numeroFourniss) {
        this.numeroFourniss = numeroFourniss;
    }

    

}
