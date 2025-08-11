import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { BonCommandeClient, CreateBccRequest } from './models/bon-commande-client.model';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class BonCommandeClientService {

  
  private apiUrl = `${environment.apiUrl}/bon-commandes-client`;
  private http = inject(HttpClient);

  getAll(): Observable<BonCommandeClient[]> {
    return this.http.get<BonCommandeClient[]>(this.apiUrl);
  }

  create(request: CreateBccRequest, pdfFile: File): Observable<BonCommandeClient> {
    const formData = new FormData();
    formData.append('bccData', new Blob([JSON.stringify(request)], { type: 'application/json' }));
    formData.append('file', pdfFile, pdfFile.name);
    return this.http.post<BonCommandeClient>(this.apiUrl, formData);
  }

    // ▼▼▼ AJOUTEZ CETTE MÉTHODE ▼▼▼
  /**
   * Supprime un bon de commande client par son ID.
   * @param id L'identifiant du bon de commande à supprimer.
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

}
