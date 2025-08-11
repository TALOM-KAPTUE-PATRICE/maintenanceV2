
import { BcFournisseur } from "./enums.model";

export interface BonCommandeFournisseur {
  id: string;
  dateCommande: string;
  dateUpdateCommande: string;
  adresseLivraison: string;
  statut: BcFournisseur; 
  devisId: string;
  fournisseurNom: string;
  fournisseurEmail: String;

}

// Correspond au CreateBonCommandeRequest de Spring
export interface CreateBcfRequest {
  devisId: string;
  fournisseurId: number;
  adresseLivraison: string;
  codeProjet: string;
  codeDevise: string;
  modeExpedition: string;
  modePaiement: string;
  moyenPaiement: string;
}

// Correspond au UpdateBonCommandeRequest de Spring
export interface UpdateBcfRequest {
  adresseLivraison: string;
  codeProjet: string;
  modeExpedition: string;
  modePaiement: string;
  moyenPaiement: string;
}

export interface BonCommandeDetailDTO extends BonCommandeFournisseur {
    montantTotal: number; // Montant total des articles liés
}