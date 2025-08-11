package com.kaptue.dev.maintenance.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateFournisseurRequest {

    @NotBlank(message = "Le nom du fournisseur ne peut pas être vide.")
    @Size(max = 255)
    private String nomFourniss;

    @NotBlank(message = "L'email du fournisseur ne peut pas être vide.")
    @Email(message = "Le format de l'email est invalide.")
    private String emailFourniss;

    @NotBlank(message = "La localisation du fournisseur est requise.")
    private String localisation;

    @NotBlank(message = "Le numéro du fournisseur est requis.")
    @Size(max = 20)
    private String numeroFourniss;

    // Getters et Setters
    public String getNomFourniss() { return nomFourniss; }
    public void setNomFourniss(String nomFourniss) { this.nomFourniss = nomFourniss; }
    public String getEmailFourniss() { return emailFourniss; }
    public void setEmailFourniss(String emailFourniss) { this.emailFourniss = emailFourniss; }
    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public String getNumeroFourniss() { return numeroFourniss; }
    public void setNumeroFourniss(String numeroFourniss) { this.numeroFourniss = numeroFourniss; }
}