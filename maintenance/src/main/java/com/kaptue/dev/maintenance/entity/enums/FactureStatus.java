package com.kaptue.dev.maintenance.entity.enums;

public enum FactureStatus {
    BROUILLON, // Brouillon, non envoyée
    EMISE,     // Envoyée au client
    PAYEE,     // Le client a payé
    EN_RETARD, // La date d'échéance est dépassée
    ANNULEE    // La facture est annulée
}

