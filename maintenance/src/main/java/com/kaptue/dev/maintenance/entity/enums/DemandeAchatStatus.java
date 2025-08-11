package com.kaptue.dev.maintenance.entity.enums;

public enum DemandeAchatStatus {
    EN_ATTENTE,   // En attente de validation par un supérieur
    APPROUVEE,    // La demande a été approuvée, on peut passer le bon de commande fournisseur
    REFUSEE,      // La demande a été refusée
    ANNULEE       // La demande a été annulée par l'initiateur
}

