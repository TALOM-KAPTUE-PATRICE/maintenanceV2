// Fichier : src/app/devis/devisService/devis.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Devis, CreateDevisRequest, UpdateDevisRequest } from '../model/devis.model';
import { environment } from '../../../environments/environment';
import { Article } from '../model/article.model';

@Injectable({
  providedIn: 'root'
})
export class DevisService {
  
  private apiUrl = `${environment.apiUrl}/devis`;

  constructor(private http: HttpClient) { }

  getAllDevis(): Observable<Devis[]> {
    return this.http.get<Devis[]>(this.apiUrl);
  }

  getDevisById(id: string): Observable<Devis> {
    return this.http.get<Devis>(`${this.apiUrl}/${id}`);
  }

  createDevis(request: CreateDevisRequest): Observable<Devis> {
    return this.http.post<Devis>(this.apiUrl, request);
  }

  updateDevis(id: string, request: UpdateDevisRequest): Observable<Devis> {
    return this.http.put<Devis>(`${this.apiUrl}/${id}`, request);
  }

  deleteDevis(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateDevisStatus(id: string, newStatus: string): Observable<Devis> {
    return this.http.put<Devis>(`${this.apiUrl}/${id}/status`, { status: newStatus });
  }

  getArticlesForDevis(devisId: string): Observable<Article[]> {
    return this.http.get<Article[]>(`${this.apiUrl}/${devisId}/articles`);
  }

  addArticleToDevis(devisId: string, articleId: number): Observable<Devis> {
    return this.http.post<Devis>(`${this.apiUrl}/${devisId}/articles/${articleId}`, {});
  }
  
  printDevis(id: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/pdf`, { responseType: 'blob' });
  }
}