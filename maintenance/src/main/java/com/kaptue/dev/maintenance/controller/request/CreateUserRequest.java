package com.kaptue.dev.maintenance.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kaptue.dev.maintenance.entity.RoleApp;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la création d'un nouvel utilisateur.
 */

@JsonIgnoreProperties(ignoreUnknown = true) 
public class CreateUserRequest {

    @NotBlank(message = "Le nom est requis")
    @Size(min = 2, max = 100)
    private String nom;

    @NotBlank(message = "L'email est requis")
    @Email(message = "Format de l'email invalide")
    private String email;

    @NotBlank(message = "Le numéro de téléphone est requis")
    private String numeroTelephone;

    @NotBlank(message = "Le mot de passe est requis")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;

    @NotNull(message = "Le rôle est requis")
    private RoleApp role;

    @NotBlank(message = "Le poste est requis")
    private String poste;

    // Pas d'imagePath ici, car il sera géré séparément avec MultipartFile

    // Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNumeroTelephone() { return numeroTelephone; }
    public void setNumeroTelephone(String numeroTelephone) { this.numeroTelephone = numeroTelephone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public RoleApp getRole() { return role; }
    public void setRole(RoleApp role) { this.role = role; }
    public String getPoste() { return poste; }
    public void setPoste(String poste) { this.poste = poste; }
}