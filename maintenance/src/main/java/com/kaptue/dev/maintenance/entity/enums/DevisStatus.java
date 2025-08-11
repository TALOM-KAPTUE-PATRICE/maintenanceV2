package com.kaptue.dev.maintenance.entity.enums;

public enum DevisStatus {
    EN_REDACTION, // Le devis est en cours de création
    SOUMIS,       // Le devis a été déposé/envoyé au client
    VALIDER,      // Le client a validé le devis (souvent via un BonCommandeClient)
    REFUSER       // Le client a refusé le devis
}