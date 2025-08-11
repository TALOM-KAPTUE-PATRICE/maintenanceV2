package com.kaptue.dev.maintenance.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailRequest {
    @NotBlank(message = "L'email est requis")
    @Email(message = "Format de l'email invalide")
    private String email;

    // Getter et Setter
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}