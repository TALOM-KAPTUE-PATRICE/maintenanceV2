import { DevisStatus } from "./enums.model";
import { Article } from "./article.model"; // On aura besoin de ce modèle pour les détails
import { Client } from "../../ticket/models/client.model";

// Correspond au DevisResponseDTO de Spring
export interface Devis {
    id: string;
    description: string;
    dateCreation: string;
    dateValidite: string;
    typeTravaux: string;
    peinture: boolean;
    effectif: number;
    devise: string;
    siteIntervention: string;
    statut: DevisStatus;
    client?: string; // Client associé, optionnel pour la liste mais utile pour les détails
    articles?: Article[]; // Liste d'articles, utile pour les détails
}

// Correspond au CreateDevisRequest de Spring
export interface CreateDevisRequest {
    description: string;
    dateValidite: string; // Format AAAA-MM-JJ
    typeTravaux: string;
    contrainte: string;
    peinture: boolean;
    effectif: number;
    livraison: string;
    devise: string;
    siteIntervention: string;
    clientId: number;
}

// Correspond au UpdateDevisRequest de Spring
export interface UpdateDevisRequest {
    description: string;
    dateValidite: string;
    typeTravaux: string;
    contrainte: string;
    peinture: boolean;
    effectif: number;
    livraison: string;
    devise: string;
    siteIntervention: string;
}