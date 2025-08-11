// src/app/models/user.model.ts
export interface User {
  id: number;            // Identifiant unique de l'utilisateur
  nom: string;          // Nom de l'utilisateur
  email: string;        // Email de l'utilisateur
  motDePasse?: string;  // Mot de passe (optionnel, selon vos besoins)
  role: string;         // Rôle de l'utilisateur (ex. 'admin', 'utilisateur', etc.)
  dateCreation?: Date;  // Date de création (optionnel)
}
