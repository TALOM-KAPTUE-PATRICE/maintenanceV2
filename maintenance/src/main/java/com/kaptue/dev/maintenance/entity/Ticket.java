package com.kaptue.dev.maintenance.entity;

// Pour la partie User de la relation
import com.fasterxml.jackson.annotation.JsonManagedReference; // Pour la partie DemandeAchat
import com.kaptue.dev.maintenance.entity.enums.TicketStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre du ticket ne peut pas être vide")
    @Size(max = 255, message = "Le titre ne doit pas dépasser 255 caractères")
    @Column(name = "titre", nullable = false) // Renommé pour la clarté
    private String titre;

    @NotBlank(message = "La description du ticket ne peut pas être vide")
    @Lob // Pour les textes potentiellement longs
    @Column(name = "description", nullable = false) // Renommé
    private String description;

    @Column(name = "date_creation", nullable = false, updatable = false) // Date de création non modifiable
    private LocalDateTime dateCreation;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private TicketStatus statut;

    @Column(name = "avancement_pourcentage")
    private Integer avancementPourcentage; // de 0 à 100

    // Relation avec Client (Un ticket appartient à un client)
    @NotNull(message = "Le client est requis pour un ticket")
    @ManyToOne(fetch = FetchType.LAZY) // Charger le client seulement si nécessaire
    @JoinColumn(name = "id_client", nullable = false)
    // @JsonBackReference("client-tickets") // Pour gérer la sérialisation si Client a une liste de tickets
    private Client client;

    // Relation avec User (Un ticket est créé/assigné à un utilisateur)
    @NotNull(message = "L'utilisateur est requis pour un ticket")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    // @JsonBackReference("user-tickets") // Pour gérer la sérialisation
    private User user;

    // Relation avec DemandeAchat (Un ticket peut avoir plusieurs demandes d'achat)
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("ticket-demandesAchats") // Pour gérer la sérialisation
    private List<DemandeAchat> demandesAchats = new ArrayList<>();

    public Ticket() {
        this.dateCreation = LocalDateTime.now();
        this.statut = TicketStatus.NOUVEAU; // Etat initial
        this.avancementPourcentage = 0; // Avancement initial

    }

    // Getters and Setters (propres)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<DemandeAchat> getDemandesAchats() {
        return demandesAchats;
    }

    public void setDemandesAchats(List<DemandeAchat> demandesAchats) {
        this.demandesAchats = demandesAchats;
    }

    // Méthodes utilitaires pour gérer la relation bidirectionnelle avec DemandeAchat
    public void addDemandeAchat(DemandeAchat demandeAchat) {
        demandesAchats.add(demandeAchat);
        demandeAchat.setTicket(this);
    }

    public void removeDemandeAchat(DemandeAchat demandeAchat) {
        demandesAchats.remove(demandeAchat);
        demandeAchat.setTicket(null);
    }

    public TicketStatus getStatut() {
        return this.statut;
    }

    public void setStatut(TicketStatus statut) {
        this.statut = statut;
    }

    public Integer getAvancementPourcentage() {
        return this.avancementPourcentage;
    }

    public void setAvancementPourcentage(Integer avancementPourcentage) {
        this.avancementPourcentage = avancementPourcentage;
    }

}
