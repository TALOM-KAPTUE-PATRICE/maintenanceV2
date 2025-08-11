
export interface Article {
  id?: number; // Optionnel pour les nouveaux articles qui n'ont pas encore d'ID
  designation: string;
  quantite: number;
  datecreationArt?: Date; // Optionnel, selon vos besoins
  prixUnitaire: number;
}


/**
 * Interface correspondant au CreateArticleRequest de Spring Boot.
 */
export interface CreateArticleRequest {
  designation: string;
  quantite: number;
  prixUnitaire: number;
  categorieId: number;
}