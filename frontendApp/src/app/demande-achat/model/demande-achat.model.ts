// Fichier : src/app/models/demande-achat.model.ts

import { DemandeAchatStatus } from "./enums.model";
// Correspond au DemandeAchatResponseDTO de Spring
export interface DemandeAchat {
  id: number;
  nomInitiateur: string;
  dateCreationDa: string;
  lieuLivraison: string;
  serviceDemandeur: string;
  objet: string;
  statut: DemandeAchatStatus;
  ticketId: number;
  ticketTitre: string;
  devisId: string;
}

// Correspond au CreateDemandeAchatRequest de Spring
export interface CreateDemandeAchatRequest {
  lieuLivraison: string;
  serviceDemandeur: string;
  objet: string;
  ticketId: number;
  devisId: string;
}

// Correspond au UpdateDemandeAchatRequest de Spring
export interface UpdateDemandeAchatRequest {
    lieuLivraison: string;
    serviceDemandeur: string;
    objet: string;
}