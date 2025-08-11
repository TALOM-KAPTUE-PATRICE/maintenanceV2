package com.kaptue.dev.maintenance.controller.response;
import com.kaptue.dev.maintenance.entity.RoleApp;
import java.util.Set;

/**
 * DTO pour renvoyer les informations d'un utilisateur (sans le mot de passe).
 */
public class UserResponseDTO {
    private Long id;
    private String nom;
    private String email;
    private String numeroTelephone;
    private RoleApp role;
    private String poste;
    private String imagePath;
    private Set<String> permissions; // Ajout des permissions

    // Constructeur, Getters, Setters
    public UserResponseDTO(Long id, String nom, String email, String numeroTelephone, RoleApp role, String poste, String imagePath, Set<String> permissions) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.numeroTelephone = numeroTelephone;
        this.role = role;
        this.poste = poste;
        this.imagePath = imagePath;
        this.permissions = permissions;
    }

    // Getters
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getEmail() { return email; }
    public String getNumeroTelephone() { return numeroTelephone; }
    public RoleApp getRole() { return role; }
    public String getPoste() { return poste; }
    public String getImagePath() { return imagePath; }
    public Set<String> getPermissions() { return permissions; }

    // Setters (généralement non nécessaires pour les DTO de réponse, mais peuvent être utiles)
}