# Maint-Pro: Plateforme de Gestion de Maintenance Industrielle



**Maint-Pro** est une application web full-stack conçue pour digitaliser et optimiser l'ensemble du cycle de vie des opérations de maintenance industrielle. Développée avec **Angular** pour le frontend et **Spring Boot** pour le backend, cette plateforme offre une solution centralisée, sécurisée et en temps réel pour remplacer les processus manuels et améliorer la prise de décision.

Ce projet a été initialement développé dans le cadre d'un stage en entreprise, avec une vision à long terme de le faire évoluer vers une solution SaaS (Software as a Service) pour les entreprises de maintenance au Cameroun et au-delà.

## 🎯 Problème Adressé

Les entreprises de maintenance industrielle font souvent face à des défis récurrents :
- **Manque de centralisation :** Informations dispersées entre des fichiers Excel, des emails et des documents papier.
- **Manque de visibilité :** Difficulté à suivre l'état d'avancement réel des projets et des interventions.
- **Processus lents et inefficaces :** Cycles de validation manuels pour les devis, les achats et la facturation.
- **Sécurité et traçabilité limitées :** Risque de perte de données et difficulté à retracer l'historique des opérations.

**Maint-Pro** a été conçu pour résoudre ces problèmes en offrant une plateforme unique et intuitive.

## ✨ Fonctionnalités Clés

L'application couvre l'intégralité du workflow de maintenance :

*   **Gestion de Projets (Tickets) :** Suivi de chaque intervention depuis la demande initiale jusqu'à la clôture, avec gestion des statuts (`Nouveau`, `En cours`, `Terminé`) et du pourcentage d'avancement.
*   **Module Commercial Complet :**
    *   **Devis :** Création de devis professionnels basés sur un catalogue d'articles et de prestations.
    *   **Bons de Commande Clients :** Enregistrement et suivi des confirmations de commande des clients, avec gestion des documents PDF.
    *   **Facturation :** Génération de factures automatiques à partir des devis validés.
*   **Processus d'Achat Optimisé :**
    *   **Demandes d'Achat :** Flux de validation interne pour les besoins en matériel.
    *   **Bons de Commande Fournisseurs :** Gestion centralisée des commandes passées aux fournisseurs.
*   **Catalogue Centralisé :** Gestion des articles, des prestations et de leurs catégories pour des chiffrages rapides et cohérents.
*   **Gestion des Tiers :** Répertoires complets pour les clients et les fournisseurs.
*   **Dashboard & KPIs :** Un tableau de bord décisionnel offrant une vue en temps réel sur les indicateurs de performance clés (état des projets, performance commerciale, etc.).
*   **Gestion des Utilisateurs et Permissions :** Un système de sécurité robuste basé sur les rôles (`ADMIN`, `USER`) et les postes (DG, Technicien, etc.), garantissant que chaque utilisateur n'accède qu'aux informations et actions qui le concernent.
*   **Notifications en Temps Réel :** Des alertes instantanées via WebSockets pour tenir les équipes informées des mises à jour importantes.

## 🛠️ Stack Technique

Ce projet met en œuvre une architecture moderne et des technologies standards de l'industrie.

### Backend (Spring Boot)
*   **Langage :** Java 21
*   **Framework :** Spring Boot 3
*   **Sécurité :** Spring Security (Authentification JWT stateless, gestion de blacklist, autorisations basées sur les permissions).
*   **Base de Données :** Spring Data JPA, Hibernate. Conçu pour MySQL (développement) et PostgreSQL (production).
*   **API :** Architecture RESTful.
*   **Notifications :** Spring WebSocket avec le protocole STOMP.
*   **Gestion de Fichiers :** Stockage des images et PDF sur un disque persistant.
*   **Déploiement :** Conteneurisé avec **Docker** et hébergé sur **Render**.

### Frontend (Angular)
*   **Framework :** Angular 19+
*   **UI :** Angular Material pour une interface utilisateur professionnelle et cohérente.
*   **Gestion d'État :** RxJS (BehaviorSubject) pour une gestion réactive de l'état d'authentification.
*   **Graphiques :** NgApexcharts pour la visualisation des données du dashboard.
*   **Routage :** Guards pour la protection des routes.
*   **Communication API :** Intercepteurs HTTP pour la gestion centralisée des tokens et des erreurs.
*   **Déploiement :** Hébergé sur **Netlify** pour une intégration et un déploiement continus.

## 🚀 Démarrage Rapide (Local)

Ce projet est un monorepo contenant le backend (`maintenance`) et le frontend (`frontendApp`).

### Prérequis
*   JDK 21
*   Maven 3.9+
*   Node.js 20+
*   Angular CLI 19+
*   Une instance MySQL locale

### Lancement du Backend
1.  Naviguez vers le dossier `maintenance`.
2.  Configurez vos identifiants de base de données dans `src/main/resources/application-dev.properties`.
3.  Exécutez `mvn spring-boot:run`.
4.  L'API sera disponible sur `http://localhost:8081`.

### Lancement du Frontend
1.  Naviguez vers le dossier `frontendApp`.
2.  Exécutez `npm install` pour installer les dépendances.
3.  Exécutez `ng serve`.
4.  L'application sera accessible sur `http://localhost:4200`.

##  Vision Future

L'architecture actuelle a été pensée pour une évolution vers un modèle **multi-tenant**, où chaque entreprise cliente disposerait de son propre espace de travail sécurisé. Les prochaines étapes incluraient :
- L'intégration d'une passerelle de paiement (ex: Stripe, solutions locales) pour la gestion des abonnements.
- La mise en place d'une isolation des données par entreprise (schéma par tenant ou discriminant).
- L'ajout de fonctionnalités de personnalisation pour que chaque entreprise puisse adapter le workflow à ses propres processus.

---
_Projet réalisé par KAPTUE PATRICE - PortFolio:  https://talom-patrice-portfolio.netlify.app/
