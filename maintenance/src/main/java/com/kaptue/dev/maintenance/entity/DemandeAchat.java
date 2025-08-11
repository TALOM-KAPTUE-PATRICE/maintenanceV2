package com.kaptue.dev.maintenance.entity;
import java.time.LocalDate;

import com.kaptue.dev.maintenance.entity.enums.DemandeAchatStatus;

import jakarta.persistence.*;



@Entity
@Table(name = "demandeAchats")
public class DemandeAchat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nomInitiateurDa")
    private String nomInitiateur;

    @Column(name ="dateCreationDa")
    private LocalDate dateCreationDa;
    
    @Column(name = "dateUpdateDa")
    private LocalDate dateUpdateDa;

    @Column(name = "lieuLivraisonDa")
    private String lieuLivraison;
    
    @Column(name = "serviceDemanadeur")
    private String serviceDemanadeur;

    @Lob
    @Column(name = "objetDa")
    private String objet;

    // Dans l'entité DemandeAchat.java

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private DemandeAchatStatus statut;

    @ManyToOne
    @JoinColumn(name = "id_ticket")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "id_devis", nullable = false) 
    private Devis devis;

    public DemandeAchat(){
        this.dateCreationDa = LocalDate.now(); 
        this.statut = DemandeAchatStatus.EN_ATTENTE;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateUpdateDa() {
        return this.dateUpdateDa;
    }

    public void setDateUpdateDa(LocalDate dateUpdateDa) {
        this.dateUpdateDa = dateUpdateDa;
    }

    public String getNomInitiateur() {
        return this.nomInitiateur;
    }

    public void setNomInitiateur(String nomInitiateur) {
        this.nomInitiateur = nomInitiateur;
    }

    public LocalDate getDateCreationDa() {
        return this.dateCreationDa;
    }

    public void setDateCreationDa(LocalDate dateCreationDa) {
        this.dateCreationDa = dateCreationDa;
    }

    public String getLieuLivraison() {
        return this.lieuLivraison;
    }

    public void setLieuLivraison(String lieuLivraison) {
        this.lieuLivraison = lieuLivraison;
    }

    public String getServiceDemandeur() {
        return this.serviceDemanadeur;
    }

    public void setServiceDemandeur(String serviceDemanadeur) {
        this.serviceDemanadeur = serviceDemanadeur;
    }

    public String getObjet() {
        return this.objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Devis getDevis() {
        return this.devis;
    }

    public void setDevis(Devis devis) {
        this.devis = devis;
    }



    public String getServiceDemanadeur() {
        return this.serviceDemanadeur;
    }

    public void setServiceDemanadeur(String serviceDemanadeur) {
        this.serviceDemanadeur = serviceDemanadeur;
    }

    public DemandeAchatStatus getStatut() {
        return this.statut;
    }

    public void setStatut(DemandeAchatStatus statut) {
        this.statut = statut;
    }

    
}
