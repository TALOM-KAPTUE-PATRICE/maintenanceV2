// fournisseur.model.ts
export interface FournisseurDTO {
    id: number;
    nomFourniss: string;
    emailFourniss: string;
    localisation: string;
    numeroFourniss: string;
  }

  export interface Link {
    name: string;
    isActive: boolean;
    url: string;
  }
    