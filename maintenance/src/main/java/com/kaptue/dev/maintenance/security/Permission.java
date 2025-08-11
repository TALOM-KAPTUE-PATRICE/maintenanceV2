package com.kaptue.dev.maintenance.security;

public final class Permission {

    // Gestion des Utilisateurs (strictement pour ADMIN)
    public static final String USER_MANAGE = "USER_MANAGE";

    // Consultation du Dashboard (pour les managers)
    public static final String DASHBOARD_VIEW = "DASHBOARD_VIEW";

    // Gestion des Tickets (Projets)
    public static final String TICKET_CREATE = "TICKET_CREATE";
    public static final String TICKET_READ_ALL = "TICKET_READ_ALL"; // Voir tous les tickets
    public static final String TICKET_READ_OWN = "TICKET_READ_OWN"; // Voir ses propres tickets assignés
    public static final String TICKET_UPDATE = "TICKET_UPDATE";
    public static final String TICKET_DELETE = "TICKET_DELETE";
    public static final String TICKET_ASSIGN = "TICKET_ASSIGN";
    public static final String TICKET_CHANGE_STATUS = "TICKET_CHANGE_STATUS";

    // Gestion des Devis
    public static final String DEVIS_CREATE = "DEVIS_CREATE";
    public static final String DEVIS_READ_ALL = "DEVIS_READ_ALL";
    public static final String DEVIS_READ_OWN = "DEVIS_READ_OWN";
    public static final String DEVIS_UPDATE = "DEVIS_UPDATE";
    public static final String DEVIS_DELETE = "DEVIS_DELETE";
    public static final String DEVIS_MANAGE_ARTICLES = "DEVIS_MANAGE_ARTICLES";
    public static final String DEVIS_CHANGE_STATUS = "DEVIS_CHANGE_STATUS";

    // Gestion des Demandes d'Achat (Interne)
    public static final String DEMANDE_ACHAT_CREATE = "DEMANDE_ACHAT_CREATE";
    public static final String DEMANDE_ACHAT_READ_ALL = "DEMANDE_ACHAT_READ_ALL";
    public static final String DEMANDE_ACHAT_CHANGE_STATUS = "DEMANDE_ACHAT_CHANGE_STATUS";
    public static final String DEMANDE_ACHAT_DELETE = "DEMANDE_ACHAT_DELETE";
    
    // Gestion des Bons de Commande CLIENT
    public static final String BON_COMMANDE_CLIENT_CREATE = "BON_COMMANDE_CLIENT_CREATE";
    public static final String BON_COMMANDE_CLIENT_READ_ALL = "BON_COMMANDE_CLIENT_READ_ALL";
    public static final String BON_COMMANDE_CLIENT_DELETE = "BON_COMMANDE_CLIENT_DELETE";

    // Gestion des Bons de Commande FOURNISSEUR
    public static final String BON_COMMANDE_FOURNISSEUR_CREATE = "BON_COMMANDE_FOURNISSEUR_CREATE";
    public static final String BON_COMMANDE_FOURNISSEUR_READ_ALL = "BON_COMMANDE_FOURNISSEUR_READ_ALL";
    public static final String BON_COMMANDE_FOURNISSEUR_UPDATE = "BON_COMMANDE_FOURNISSEUR_UPDATE";
    public static final String BON_COMMANDE_FOURNISSEUR_DELETE = "BON_COMMANDE_FOURNISSEUR_DELETE";

    // Gestion des Factures
    public static final String FACTURE_CREATE = "FACTURE_CREATE";
    public static final String FACTURE_READ_ALL = "FACTURE_READ_ALL";
    public static final String FACTURE_UPDATE = "FACTURE_UPDATE";
    public static final String FACTURE_DELETE = "FACTURE_DELETE";

    // Gestion du Catalogue (Articles & Catégories)
    public static final String CATALOGUE_READ = "CATALOGUE_READ";
    public static final String CATALOGUE_MANAGE = "CATALOGUE_MANAGE";

    // Gestion des Clients
    public static final String CLIENT_CREATE = "CLIENT_CREATE";
    public static final String CLIENT_READ_ALL = "CLIENT_READ_ALL";
    public static final String CLIENT_UPDATE = "CLIENT_UPDATE";
    public static final String CLIENT_DELETE = "CLIENT_DELETE";

    // Gestion des Fournisseurs
    public static final String FOURNISSEUR_CREATE = "FOURNISSEUR_CREATE";
    public static final String FOURNISSEUR_READ_ALL = "FOURNISSEUR_READ_ALL";
    public static final String FOURNISSEUR_UPDATE = "FOURNISSEUR_UPDATE";
    public static final String FOURNISSEUR_DELETE = "FOURNISSEUR_DELETE";

    private Permission() {}
}