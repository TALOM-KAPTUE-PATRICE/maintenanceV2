package com.kaptue.dev.maintenance.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.kaptue.dev.maintenance.entity.enums.BcFournisseur;

@Entity
@Table(name = "bonCommandes")
public class BonCommandeFournisseur {
    
    @Id
    private String id; // Identifiant au format personnalisé

    @Column(name = "dateCommande")
    private LocalDate dateCommande;
    
    @Column(name = "dateUpdateCommande")
    private LocalDate dateUpdateCommande;

    @Column(name= "codeProjet")
    private String codeProjet;

    @Column(name = "codeDevise", nullable = false)
    private String codeDevise;

    @Column(name = "adresseLivraison", nullable = false)
    private String adresseLivraison;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private BcFournisseur statut;
     
    @Column(name = "modeExpedition")
    private String modeExpedition;

    @Column(name= "modePaiement")
    private String modePaiement;

    @Column(name = "moyenPaiement")
    private String moyenPaiement;

    @Column(name = "emailBe")
    private String emailBe;

    @ManyToOne
    @JoinColumn(name = "id_Fournisseur")
    private Fournisseur fournisseur;

    @ManyToOne
    @JoinColumn(name = "id_devis", nullable = false)
    private Devis devis;

    // Ajoutez d'autres attributs nécessaires ici


    public BonCommandeFournisseur() {
        this.statut = BcFournisseur.LIVRER_PARTIELLEMENT;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public BcFournisseur getStatut() {
        return this.statut;
    }

    public void setStatut(BcFournisseur statut) {
        this.statut = statut;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public LocalDate getDateUpdateCommande() {
        return this.dateUpdateCommande;
    }

    public void setDateUpdateCommande(LocalDate dateUpdateCommande) {
        this.dateUpdateCommande = dateUpdateCommande;
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

    public Fournisseur getFournisseur() {
        return this.fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Devis getDevis() {
        return this.devis;
    }

    public void setDevis(Devis devis) {
        this.devis = devis;
    }
}