import { TicketStatus } from "./enums.model"; // Nous créerons ce fichier ensuite

// Correspond au TicketResponseDTO de Spring
export interface Ticket {
  id: number;
  titre: string;
  description: string;
  dateCreation: string; // Garder en string pour la simplicité, Angular le formatera
  statut: TicketStatus;
  avancementPourcentage: number;
  clientNom: string;
  userNom: string;
}

// Correspond au CreateTicketRequest de Spring
export interface CreateTicketRequest {
  titre: string;
  description: string;
  clientId: number;
  userId?: number; // L'ID de l'utilisateur assigné, optionnel
}

// Correspond au UpdateTicketRequest de Spring
export interface UpdateTicketRequest {
  titre: string;
  description: string;
  clientId: number;
  userId: number;
}