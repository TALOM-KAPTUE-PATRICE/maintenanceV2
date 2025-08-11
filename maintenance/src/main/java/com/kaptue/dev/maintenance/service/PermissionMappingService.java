package com.kaptue.dev.maintenance.service;

import com.kaptue.dev.maintenance.entity.RoleApp;
import com.kaptue.dev.maintenance.entity.User;
import com.kaptue.dev.maintenance.security.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

@Service
public class PermissionMappingService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionMappingService.class);

    // Définition claire des postes
    public static final String POSTE_DG = "DG";
    public static final String POSTE_ASSISTANTE_DIRECTION = "ASSISTANTE_DIRECTION";
    public static final String POSTE_RESPONSABLE_COMMERCIAL = "RESPONSABLE_COMMERCIAL";
    public static final String POSTE_RESPONSABLE_ACHAT = "RESPONSABLE_ACHAT";
    public static final String POSTE_RESPONSABLE_TECHNIQUE = "RESPONSABLE_TECHNIQUE";
    public static final String POSTE_TECHNICIEN_MAINTENANCE = "TECHNICIEN_MAINTENANCE";

    public Set<String> getPermissionsForUser(User user) {
        if (user == null) {
            return Collections.emptySet();
        }

        RoleApp role = user.getRole();
        String poste = user.getPoste();
        logger.debug("Détermination des permissions pour l'utilisateur: {}, Rôle: {}, Poste: {}", user.getEmail(), role, poste);

        // CAS 1 : Rôle ADMIN
        if (role == RoleApp.ADMIN) {
            logger.info("Attribution des permissions d'ADMIN pour {}", user.getEmail());
            return Set.of(Permission.USER_MANAGE); // L'ADMIN gère UNIQUEMENT les utilisateurs.
        }

        // CAS 2 : Rôle USER (avec un poste métier)
        if (role == RoleApp.USER && poste != null && !poste.isBlank()) {
            return getPermissionsForPoste(poste.toUpperCase(), user.getEmail());
        }

        // CAS 3 : Aucun rôle ou poste défini -> aucune permission
        logger.warn("Utilisateur {} sans rôle ou poste valide. Aucune permission métier attribuée.", user.getEmail());
        return Collections.emptySet();
    }

    private Set<String> getPermissionsForPoste(String poste, String email) {
        Set<String> permissions = new HashSet<>();
        logger.info("Attribution des permissions pour le poste {} à l'utilisateur {}", poste, email);

        switch (poste) {
            case POSTE_DG:
                // Le DG a une vue sur tout et peut tout approuver/gérer
                permissions.addAll(getDirecteurPermissions());
                break;
            
            case POSTE_ASSISTANTE_DIRECTION:
                permissions.add(Permission.DASHBOARD_VIEW);
                permissions.add(Permission.CLIENT_READ_ALL);
                permissions.add(Permission.CLIENT_CREATE);
                permissions.add(Permission.CLIENT_UPDATE);
                permissions.add(Permission.FACTURE_READ_ALL);
                permissions.add(Permission.FACTURE_CREATE);
                permissions.add(Permission.FACTURE_UPDATE);
                permissions.add(Permission.DEVIS_READ_ALL);
                break;

            case POSTE_RESPONSABLE_COMMERCIAL:
                permissions.add(Permission.CLIENT_READ_ALL);
                permissions.add(Permission.CLIENT_CREATE);
                permissions.add(Permission.CLIENT_UPDATE);
                permissions.add(Permission.DEVIS_READ_ALL);
                permissions.add(Permission.DEVIS_CREATE);
                permissions.add(Permission.DEVIS_UPDATE);
                permissions.add(Permission.DEVIS_MANAGE_ARTICLES);
                permissions.add(Permission.DEVIS_CHANGE_STATUS);
                permissions.add(Permission.BON_COMMANDE_CLIENT_READ_ALL);
                permissions.add(Permission.BON_COMMANDE_CLIENT_CREATE);
                permissions.add(Permission.CATALOGUE_READ);
                break;

            case POSTE_RESPONSABLE_ACHAT:
                permissions.add(Permission.FOURNISSEUR_READ_ALL);
                permissions.add(Permission.FOURNISSEUR_CREATE);
                permissions.add(Permission.FOURNISSEUR_UPDATE);
                permissions.add(Permission.DEMANDE_ACHAT_READ_ALL);
                permissions.add(Permission.DEMANDE_ACHAT_CHANGE_STATUS); // Approuve/Refuse les DA
                permissions.add(Permission.BON_COMMANDE_FOURNISSEUR_READ_ALL);
                permissions.add(Permission.BON_COMMANDE_FOURNISSEUR_CREATE);
                permissions.add(Permission.BON_COMMANDE_FOURNISSEUR_UPDATE);
                permissions.add(Permission.CATALOGUE_MANAGE); // Gère le catalogue
                break;

            case POSTE_RESPONSABLE_TECHNIQUE:
                permissions.add(Permission.DASHBOARD_VIEW);
                permissions.add(Permission.TICKET_READ_ALL);
                permissions.add(Permission.TICKET_CREATE);
                permissions.add(Permission.TICKET_ASSIGN); // Peut assigner les tickets
                permissions.add(Permission.TICKET_CHANGE_STATUS); // Peut changer l'état des projets
                permissions.add(Permission.DEMANDE_ACHAT_CREATE); // Peut initier une DA pour un projet
                permissions.add(Permission.DEVIS_READ_ALL);
                break;

            case POSTE_TECHNICIEN_MAINTENANCE:
                permissions.add(Permission.TICKET_READ_OWN); // Ne voit que ses tickets
                permissions.add(Permission.TICKET_UPDATE); // Peut mettre à jour ses propres tickets
                permissions.add(Permission.CATALOGUE_READ);
                break;

            default:
                logger.warn("Poste non reconnu: {}. Aucune permission spécifique attribuée.", poste);
                break;
        }
        return permissions;
    }

    
    private Set<String> getDirecteurPermissions() {
        // Le DG a presque toutes les permissions de lecture et de gestion
        return Set.of(
            Permission.DASHBOARD_VIEW,
            Permission.USER_MANAGE, // Le DG peut aussi gérer les utilisateurs
            Permission.TICKET_READ_ALL, Permission.TICKET_ASSIGN, Permission.TICKET_CHANGE_STATUS, Permission.TICKET_DELETE,Permission.TICKET_CREATE, Permission.TICKET_UPDATE, Permission.TICKET_READ_OWN,
            Permission.DEVIS_READ_ALL, Permission.DEVIS_CHANGE_STATUS, Permission.DEVIS_DELETE, Permission.DEVIS_CREATE, Permission.DEVIS_MANAGE_ARTICLES, Permission.DEVIS_READ_OWN, Permission.DEVIS_UPDATE,
            Permission.DEMANDE_ACHAT_READ_ALL, Permission.DEMANDE_ACHAT_CHANGE_STATUS, Permission.DEMANDE_ACHAT_DELETE, Permission.DEMANDE_ACHAT_CREATE,
            Permission.BON_COMMANDE_CLIENT_READ_ALL, Permission.BON_COMMANDE_CLIENT_DELETE, Permission.BON_COMMANDE_CLIENT_CREATE,
            Permission.BON_COMMANDE_FOURNISSEUR_READ_ALL, Permission.BON_COMMANDE_FOURNISSEUR_DELETE, Permission.BON_COMMANDE_FOURNISSEUR_UPDATE, Permission.BON_COMMANDE_FOURNISSEUR_CREATE,
            Permission.FACTURE_READ_ALL, Permission.FACTURE_DELETE,
            Permission.CLIENT_READ_ALL, Permission.CLIENT_DELETE,
            Permission.FOURNISSEUR_READ_ALL, Permission.FOURNISSEUR_DELETE,
            Permission.CATALOGUE_MANAGE, Permission.CATALOGUE_READ
        );
    }
}