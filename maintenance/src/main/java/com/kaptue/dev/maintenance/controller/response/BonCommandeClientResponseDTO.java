package com.kaptue.dev.maintenance.controller.response;

import com.kaptue.dev.maintenance.entity.BonCommandeClient;
import java.time.LocalDate;

public class BonCommandeClientResponseDTO {
    private Long id;
    private String referenceClient;
    private LocalDate dateReception;
    private String devisId;
    private String clientNom; // Utile pour l'affichage
    private String pdfUrl; // URL pour télécharger/voir le PDF
    // Getters et Setters...

    public static BonCommandeClientResponseDTO fromEntity(BonCommandeClient bcc, String baseUrl) {
        BonCommandeClientResponseDTO dto = new BonCommandeClientResponseDTO();
        // ... mapper les champs id, referenceClient, dateReception ...

        dto.setReferenceClient(bcc.getReferenceClient());
        dto.setDateReception(bcc.getDateReception());
        dto.setId(bcc.getId());

        if (bcc.getDevis() != null) {
            dto.setDevisId(bcc.getDevis().getId());
            if (bcc.getDevis().getClient() != null) {
                dto.setClientNom(bcc.getDevis().getClient().getNom());
            }
        }
        if (bcc.getPdfPath() != null && !bcc.getPdfPath().isBlank()) {
            dto.setPdfUrl(baseUrl + "/" + bcc.getPdfPath());
        }
        return dto;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getClientNom() {
        return this.clientNom;
    }

    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }

    public String getPdfUrl() {
        return this.pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

}