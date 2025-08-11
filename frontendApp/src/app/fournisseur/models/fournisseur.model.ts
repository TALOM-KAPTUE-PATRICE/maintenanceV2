/**
 * Interface représentant la structure d'un Fournisseur
 * telle qu'elle est retournée par le FournisseurResponseDTO de Spring.
 */
export interface Fournisseur {
  id: number;
  nomFourniss: string;
  emailFourniss: string;
  localisation: string;
  numeroFourniss: string;
}

/**
 * Interface pour la création d'un Fournisseur,
 * correspondant au CreateFournisseurRequest de Spring.
 */
export interface CreateFournisseurRequest {
  nomFourniss: string;
  emailFourniss: string;
  localisation: string;
  numeroFourniss: string;
}

/**
 * Interface pour la mise à jour d'un Fournisseur,
 * correspondant au UpdateFournisseurRequest de Spring.
 */
export interface UpdateFournisseurRequest {
  nomFourniss: string;
  emailFourniss: string;
  localisation: string;
  numeroFourniss: string;
}