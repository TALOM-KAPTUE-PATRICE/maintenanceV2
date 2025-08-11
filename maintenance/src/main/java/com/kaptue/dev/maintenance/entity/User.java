package com.kaptue.dev.maintenance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size; // Pour la taille du mot de passe par exemple
import java.util.List;

/**
 * Entité représentant un utilisateur de l'application.
 */
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email") // Assurer l'unicité de l'email au niveau BD
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom ne peut pas être vide")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "Format de l'email invalide")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Le numéro de téléphone ne peut pas être vide")
    @Size(min = 9, max = 20, message = "Le numéro de téléphone doit être valide")
    private String numeroTelephone;

    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    // La validation de la taille du mot de passe se fait souvent avant le hachage
    // JsonIgnore pour ne jamais l'envoyer dans les réponses API
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleApp role; // Rôle global (ex: ADMIN, USER)

    @NotBlank(message = "Le poste ne peut pas être vide")
    @Column(nullable = false)
    private String poste; // Poste spécifique dans l'entreprise (ex: DG, TECHNICIEN_MAINTENANCE)

    private String imagePath; // Chemin vers l'image de profil de l'utilisateur

    // Relations (bidirectionnelles, gérées par l'autre entité avec mappedBy)
    // JsonIgnore pour éviter les boucles infinies lors de la sérialisation
    // et pour ne pas charger inutilement ces listes sauf si explicitement demandé.

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Notification> notifications; // Renommé en 'notifications' pour la clarté

    // Constructeurs
    public User() {
    }

    // Getters et Setters (générés ou écrits proprement)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroTelephone() {
        return numeroTelephone;
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleApp getRole() {
        return role;
    }

    public void setRole(RoleApp role) {
        this.role = role;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    // equals() et hashCode() devraient être implémentés si vous manipulez des User dans des Sets ou Maps,
    // typiquement basés sur l'ID après persistance.
}