import { TicketStatus } from "../../ticket/models/enums.model";


// KPI 1: État d'un projet
export interface ProjectStatusKpi {
  id: number;
  titre: string;
  statut: TicketStatus;
  avancementPourcentage: number;
  clientNom: string;
}

// KPI 2: Données pour les graphiques
export interface ChartData<T> {
  label: T;
  value: number;
}