// NOUVEAU : Interface pour gérer la pagination de Spring Boot
export interface Page<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number; // Numéro de la page actuelle
}