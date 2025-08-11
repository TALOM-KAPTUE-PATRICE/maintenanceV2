package com.kaptue.dev.maintenance.controller.response;

import com.kaptue.dev.maintenance.entity.Client;

public class ClientResponseDTO {

    private Long id;
    private String nom;
    private String email;
    private String numero;

    public static ClientResponseDTO fromEntity(Client client) {
        if (client == null) {
            return null;
        }
        ClientResponseDTO dto = new ClientResponseDTO();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setEmail(client.getEmail());
        dto.setNumero(client.getNumero());
        return dto;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
}