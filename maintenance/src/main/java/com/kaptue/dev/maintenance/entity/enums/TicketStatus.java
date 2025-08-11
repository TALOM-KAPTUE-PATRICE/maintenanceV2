package com.kaptue.dev.maintenance.entity.enums;

public enum TicketStatus {
    NOUVEAU,      // Le projet vient d'être créé
    EN_COURS,     // Le travail a commencé
    EN_ARRET,     // Le projet est en pause (en attente d'une pièce, d'une validation...)
    TERMINE,      // Le travail est achevé
    CLOTURE       // La facturation est faite, le projet est archivé
}