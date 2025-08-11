import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Categorie } from '../models/categorie.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CategorieService {
  private apiUrl = `${environment.apiUrl}/categories`;

  constructor(private http: HttpClient) { }

  /**
   * Récupère la liste complète des catégories.
   */
  
  getAllCategories(): Observable<Categorie[]> {
    return this.http.get<Categorie[]>(this.apiUrl);
  }
}