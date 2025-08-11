// Ce n'est pas un DTO JSON, mais un objet pour le service. Le contrôleur le construira.
package com.kaptue.dev.maintenance.controller.request;

import java.time.LocalDate;

public class CreateBonCommandeClientRequest {
    private String referenceClient;
    private LocalDate dateReception;
    private String devisId;
    private String notes;
    // Getters et Setters...

    public String getReferenceClient() {
        return this.referenceClient;
    }

    public void setReferenceClient(String referenceClient) {
        this.referenceClient = referenceClient;
    }

    public LocalDate getDateReception() {
        return this.dateReception;
    }

    public void setDateReception(LocalDate dateReception) {
        this.dateReception = dateReception;
    }

    public String getDevisId() {
        return this.devisId;
    }

    public void setDevisId(String devisId) {
        this.devisId = devisId;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}