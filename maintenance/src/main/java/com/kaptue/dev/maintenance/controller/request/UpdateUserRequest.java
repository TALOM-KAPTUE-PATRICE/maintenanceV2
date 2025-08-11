package com.kaptue.dev.maintenance.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kaptue.dev.maintenance.entity.RoleApp; // Assurez-vous que le chemin est correct
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la mise à jour des informations d'un utilisateur.
 * Les champs sont optionnels : seuls les champs non nuls/non vides dans la requête
 * seront considérés pour la mise à jour.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserRequest {

    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @Email(message = "Format de l'email invalide")
    private String email;

    // Pas de @NotBlank ici car le champ peut ne pas être mis à jour
    @Size(min = 9, max = 20, message = "Le numéro de téléphone doit être valide s'il est fourni")
    private String numeroTelephone;

    private RoleApp role;

    // Pas de @NotBlank ici car le champ peut ne pas être mis à jour
    private String poste;

    // Le mot de passe n'est généralement pas mis à jour via ce DTO,
    // mais via une fonctionnalité dédiée "changer mot de passe".
    // L'image est gérée séparément via MultipartFile dans le contrôleur.

    // --- Getters ---
    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getNumeroTelephone() {
        return numeroTelephone;
    }

    public RoleApp getRole() {
        return role;
    }

    public String getPoste() {
        return poste;
    }

    // --- Setters ---
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public void setRole(RoleApp role) {
        this.role = role;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }
}