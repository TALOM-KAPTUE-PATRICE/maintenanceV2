// fournisseur.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FournisseurDTO } from '../models/fournisseur.model';

@Injectable({
  providedIn: 'root'
})
export class ServiceAppService {
  
  private apiUrl = 'http://localhost:8081/fournisseurs';
  private apiUrlClt = 'http://localhost:8081/clients';


  constructor(private http: HttpClient) {}

  // Get all suppliers
  getAllFournisseurs(): Observable<FournisseurDTO[]> {
    return this.http.get<FournisseurDTO[]>(`${this.apiUrl}/all`);
  }

  // Create a new supplier
  createFournisseur(fournisseur: FournisseurDTO): Observable<FournisseurDTO> {
    return this.http.post<FournisseurDTO>(`${this.apiUrl}/create`, fournisseur);
  }

  getAllClients(): Observable<any> {
    return this.http.get<any>(`${this.apiUrlClt}/all`);
  }

  // Update an existing supplier
  updateFournisseur(id: number, fournisseur: FournisseurDTO): Observable<FournisseurDTO> {
    return this.http.put<FournisseurDTO>(`${this.apiUrl}/update/${id}`, fournisseur);
  }

  // Delete a supplier
  deleteFournisseur(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  // Get supplier by ID
  getFournisseurById(id: number): Observable<FournisseurDTO> {
    return this.http.get<FournisseurDTO>(`${this.apiUrl}/id/${id}`);
  }
}
