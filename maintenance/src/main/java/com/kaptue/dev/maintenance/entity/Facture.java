package com.kaptue.dev.maintenance.entity;

import java.time.LocalDate;

import com.kaptue.dev.maintenance.entity.enums.FactureStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "factures")

public class Facture {
    @Id
    private String id;

    @Column(name = "montant")
    private double montant;

    @Column(name = "numeroSecretariat")
    private String numeroSecretariat;

    @Column(name = "emailBE")
    private String emailBE;

    @Column(name = "numBetang")
    private String numBetang;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FactureStatus statut;

    @Column(name = "dateFacturation")
    private LocalDate dateFacturation;

    @Column(name = "dateUpdateFacture")
    private LocalDate dateUpdateFacture;

    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_devis")
    private Devis devis;
    

    public Facture() {
        this.statut = FactureStatus.BROUILLON; // Statut initial par défaut
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDateUpdateFacture() {
        return this.dateUpdateFacture;
    }

    public void setDateUpdateFacture(LocalDate dateUpdateFacture) {
        this.dateUpdateFacture = dateUpdateFacture;
    }

    public FactureStatus getStatut() {
        return this.statut;
    }

    public void setStatut(FactureStatus statut) {
        this.statut = statut;
    }

    public double getMontant() {
        return this.montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDate getDateFacturation() {
        return this.dateFacturation;
    }

    public void setDateFacturation(LocalDate dateFacturation) {
        this.dateFacturation = dateFacturation;
    }

    public String getNumeroSecretariat() {
        return this.numeroSecretariat;
    }

    public void setNumeroSecretariat(String numeroSecretariat) {
        this.numeroSecretariat = numeroSecretariat;
    }

    public String getEmailBE() {
        return this.emailBE;
    }

    public void setEmailBE(String emailBE) {
        this.emailBE = emailBE;
    }

    public String getNumBetang() {
        return this.numBetang;
    }

    public void setNumBetang(String numBetang) {
        this.numBetang = numBetang;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Devis getDevis() {
        return this.devis;
    }

    public void setDevis(Devis devis) {
        this.devis = devis;
    }
}
