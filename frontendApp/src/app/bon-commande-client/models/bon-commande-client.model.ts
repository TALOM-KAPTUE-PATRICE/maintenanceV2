export interface BonCommandeClient {
  id: number;
  referenceClient: string;
  dateReception: string;
  devisId: string;
  clientNom: string;
  pdfUrl?: string; // Optionnel
}

export interface CreateBccRequest {
  referenceClient: string;
  dateReception: string; // ISO String
  devisId: string;
  notes: string;
}