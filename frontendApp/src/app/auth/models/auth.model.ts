// src/app/auth/models/auth.model.ts (ou un dossier partagé 'models')

export interface UserProfile { // Correspond au userDto de la réponse Spring Boot
  id: number;
  nom: string;
  email: string;
  numeroTelephone?: string; // Optionnel si pas toujours présent
  role: string; // Ex: 'USER', 'ADMIN'
  poste: string; // Ex: 'TECHNICIEN_MAINTENANCE', 'DG'
  imagePath?: string;
  permissions: string[];
}

export interface AuthResponse {
  token: string;
  user: UserProfile;
}


// export interface AuthResponse {
//   token: string;
 
// }

export interface GoogleResponse {
  credential: string;
  
}
