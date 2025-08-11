package com.kaptue.dev.maintenance.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateClientRequest {

    @NotBlank(message = "Le nom du client ne peut pas être vide.")
    @Size(max = 100, message = "Le nom du client ne doit pas dépasser 100 caractères.")
    private String nom;

    @NotBlank(message = "L'email du client ne peut pas être vide.")
    @Email(message = "Le format de l'email est invalide.")
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères.")
    private String email;

    @NotBlank(message = "Le numéro de téléphone du client ne peut pas être vide.")
    @Size(max = 20, message = "Le numéro de téléphone ne doit pas dépasser 20 caractères.")
    private String numero;

    // Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
}