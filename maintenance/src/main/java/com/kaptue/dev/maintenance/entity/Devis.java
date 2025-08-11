package com.kaptue.dev.maintenance.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kaptue.dev.maintenance.entity.enums.DevisStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;




@Entity
@Table(name = "devis")
public class Devis {
    @Id 
    private String id;

    @Lob
    @Column(name = "descriptionDevis")
    private String description;

    @Column(name = "dateCreationDevis")
    private LocalDate dateCreation;

    @Column(name = "dateUpdateDevis")
    private LocalDate dateUpdateDevis;


    @Column(name = "dateValiditeDevis")
    private LocalDate dateValidite;

    @Column(name = "typeTravaux")
    private String typeTravaux;

    @Column( name="contrainteDevis")
    private String contrainte;

    @Column(name=" peinture")
    private boolean  peinture;

    @Column(name= "effectif")
    private int effectif;

    @Column(name= "livraison")
    private String livraison;

    @Column(name = "devise")
    private String devise;

    @Column(name = "siteIntervention")
    private String siteIntervention;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private DevisStatus statut; 

        // ▼▼▼ AJOUTEZ CETTE RELATION ▼▼▼
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client") // Nom de la colonne de la clé étrangère dans la table 'devis'
    private Client client;
    
    @OneToMany(mappedBy = "devis")
    @JsonIgnore
    private List<DemandeAchat> demandeAchats;

    @OneToMany(mappedBy = "devis")
    @JsonIgnore
    private List<Facture> factures;

    @OneToMany(mappedBy = "devis")
    private List<BonCommandeFournisseur> bonCommandes;

    // Dans Devis.java
    @OneToOne(mappedBy = "devis", fetch = FetchType.LAZY)
    private BonCommandeClient bonCommandeClient;



    @ManyToMany
    @JoinTable(
        name = "devis_article",
        joinColumns = @JoinColumn(name = "id_devis"),
        inverseJoinColumns = @JoinColumn(name = "id_article")
    )
    @JsonBackReference
    private Set<Article> articles = new HashSet<>();

        // Pensez à ajouter un constructeur ou initialiser statut = DevisStatus.EN_REDACTION
    public Devis() {
        this.statut = DevisStatus.EN_REDACTION;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DemandeAchat> getDemandeAchats() {
        return this.demandeAchats;
    }

    public void setDemandeAchats(List<DemandeAchat> demandeAchats) {
        this.demandeAchats = demandeAchats;
    }

    public List<Facture> getFactures() {
        return this.factures;
    }

    public void setFactures(List<Facture> factures) {
        this.factures = factures;
    }

    public List<BonCommandeFournisseur> getBonCommandes() {
        return this.bonCommandes;
    }

    public void setBonCommandes(List<BonCommandeFournisseur> bonCommandes) {
        this.bonCommandes = bonCommandes;
    }

    public Set<Article> getArticles() {
        return this.articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
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

    public LocalDate getDateUpdateDevis() {
        return this.dateUpdateDevis;
    }

    public void setDateUpdateDevis(LocalDate dateUpdateDevis) {
        this.dateUpdateDevis = dateUpdateDevis;
    }


    public LocalDate getDateValidite() {
        return this.dateValidite;
    }

    public void setDateValidite(LocalDate dateValidite) {
        this.dateValidite = dateValidite;
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

    public boolean isPeinture() {
        return this.peinture;
    }

    public boolean getPeinture() {
        return this.peinture;
    }

    public void setPeinture(boolean peinture) {
        this.peinture = peinture;
    }

    public int getEffectif() {
        return this.effectif;
    }

    public void setEffectif(int effectif) {
        this.effectif = effectif;
    }

    public String getLivraison() {
        return this.livraison;
    }

    public void setLivraison(String livraison) {
        this.livraison = livraison;
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
  

    public DevisStatus getStatut() {
        return this.statut;
    }

    public void setStatut(DevisStatus statut) {
        this.statut = statut;
    }


    public BonCommandeClient getBonCommandeClient() {
        return this.bonCommandeClient;
    }

    public void setBonCommandeClient(BonCommandeClient bonCommandeClient) {
        this.bonCommandeClient = bonCommandeClient;
    }


    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    
}
