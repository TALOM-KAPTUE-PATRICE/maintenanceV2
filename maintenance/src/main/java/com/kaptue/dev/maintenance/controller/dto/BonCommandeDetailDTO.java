package com.kaptue.dev.maintenance.controller.dto;




public class BonCommandeDetailDTO extends BonCommandeDTO {
    private double montantTotal; // Montant total des articles liés

    // Getters et Setters

    public double getMontantTotal() {
        return this.montantTotal;
    }
    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }
    // ...
}
