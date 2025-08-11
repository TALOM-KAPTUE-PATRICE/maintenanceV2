

export enum RoleApp {
  ADMIN = 'ADMIN',
  USER = 'USER'
}

// Correspond à UserResponseDTO
export interface User {
  id: number;
  nom: string;
  email: string;
  numeroTelephone: string;
  role: RoleApp;
  poste: string;
  imagePath?: string;
  permissions: string[];
}

// Correspond à CreateUserRequest
export interface CreateUserRequest {
  nom: string;
  email: string;
  numeroTelephone: string;
  password?: string;
  role: RoleApp;
  poste: string;
}

// Correspond à UpdateUserRequest
export interface UpdateUserRequest {
  nom: string;
  email: string;
  numeroTelephone: string;
  role: RoleApp;
  poste: string;
}