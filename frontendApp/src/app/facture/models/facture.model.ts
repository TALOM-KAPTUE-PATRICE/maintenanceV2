export enum FactureStatus {
  BROUILLON = 'BROUILLON',
  EMISE = 'EMISE',
  PAYEE = 'PAYEE',
  EN_RETARD = 'EN_RETARD',
  ANNULEE = 'ANNULEE'
  
}

// Correspond au FactureResponseDTO
export interface Facture {
  id: string;
  montant: number;
  dateFacturation: string;
  dateUpdateFacture: string;
  numeroSecretariat: string;
  clientNom: string;
  devisId: string;
  statut: FactureStatus;
}

// Correspond au CreateFactureRequest
export interface CreateFactureRequest {
  devisId: string;
  clientId: number;
  numeroSecretariat: string;
  emailBE?: string;
  numBetang?: string;
}

// Correspond au UpdateFactureRequest
export interface UpdateFactureRequest {
  numeroSecretariat: string;
  emailBE?: string;
  numBetang?: string;
}