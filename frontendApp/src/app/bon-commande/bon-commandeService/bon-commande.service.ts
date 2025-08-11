
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { BonCommandeFournisseur, CreateBcfRequest, UpdateBcfRequest } from '../model/bon-commande-fournisseur.model';

@Injectable({
  providedIn: 'root'
})
export class BonCommandeFournisseurService {
  private apiUrl = `${environment.apiUrl}/bon-commandes`; // L'endpoint RESTful
  private http = inject(HttpClient);

  getAll(): Observable<BonCommandeFournisseur[]> {
    return this.http.get<BonCommandeFournisseur[]>(this.apiUrl);
  }

  getById(id: string): Observable<BonCommandeFournisseur> {
    return this.http.get<BonCommandeFournisseur>(`${this.apiUrl}/${id}`);
  }

  create(request: CreateBcfRequest): Observable<BonCommandeFournisseur> {
    return this.http.post<BonCommandeFournisseur>(this.apiUrl, request);
  }

  update(id: string, request: UpdateBcfRequest): Observable<BonCommandeFournisseur> {
    return this.http.put<BonCommandeFournisseur>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  printPdf(id: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/pdf`, { responseType: 'blob' });
  }
}