import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Fournisseur, CreateFournisseurRequest, UpdateFournisseurRequest } from './models/fournisseur.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root' 
})
export class FournisseurService {
  private apiUrl = `${environment.apiUrl}/fournisseurs`;
  private http = inject(HttpClient);

  /**
   * Récupère la liste complète de tous les fournisseurs.
   * Idéal pour les listes déroulantes.
   */
  getAll(): Observable<Fournisseur[]> {
    // Le backend pour les fournisseurs retourne une Page, donc on demande une grande page
    // pour simuler la récupération de tout.
    return this.http.get<Fournisseur[]>(this.apiUrl);
  }

  /**
   * Récupère un fournisseur par son ID.
   * @param id L'identifiant du fournisseur.
   */
  getById(id: number): Observable<Fournisseur> {
    return this.http.get<Fournisseur>(`${this.apiUrl}/${id}`);
  }

  /**
   * Crée un nouveau fournisseur.
   * @param request Les données du fournisseur à créer.
   */
  create(request: CreateFournisseurRequest): Observable<Fournisseur> {
    return this.http.post<Fournisseur>(this.apiUrl, request);
  }

  /**
   * Met à jour un fournisseur existant.
   * @param id L'identifiant du fournisseur à mettre à jour.
   * @param request Les nouvelles données du fournisseur.
   */
  update(id: number, request: UpdateFournisseurRequest): Observable<Fournisseur> {
    return this.http.put<Fournisseur>(`${this.apiUrl}/${id}`, request);
  }

  /**
   * Supprime un fournisseur.
   * @param id L'identifiant du fournisseur à supprimer.
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}