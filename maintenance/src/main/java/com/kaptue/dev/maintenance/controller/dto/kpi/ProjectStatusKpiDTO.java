package com.kaptue.dev.maintenance.controller.dto.kpi;

import com.kaptue.dev.maintenance.entity.enums.TicketStatus;

public class ProjectStatusKpiDTO {
    private Long id;
    private String titre;
    private TicketStatus statut;
    private Integer avancementPourcentage;
    private String clientNom;
    // Getters, Setters


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
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

    public String getClientNom() {
        return this.clientNom;
    }

    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }

}