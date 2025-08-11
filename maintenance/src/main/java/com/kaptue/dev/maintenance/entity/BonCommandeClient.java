// Dans package com.kaptue.dev.maintenance.entity
package com.kaptue.dev.maintenance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name = "bon_commande_client")
public class BonCommandeClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La référence du bon de commande client est requise.")
    @Column(name = "reference_client", nullable = false)
    private String referenceClient; // Le numéro de PO du client (ex: "PO-SABC-12345")

    @Column(name = "date_reception", nullable = false)
    private LocalDate dateReception;

    @Column(name = "pdf_path")
    private String pdfPath; // Chemin relatif vers le fichier PDF stocké

    // Un bon de commande client valide UN devis spécifique.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_devis", unique = true, nullable = false)
    private Devis devis;

    @Column(name = "notes")
    @Lob
    private String notes;

    // Getters et Setters...\

    public String getPdfPath() {
        return this.pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
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

    public Devis getDevis() {
        return this.devis;
    }

    public void setDevis(Devis devis) {
        this.devis = devis;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


}