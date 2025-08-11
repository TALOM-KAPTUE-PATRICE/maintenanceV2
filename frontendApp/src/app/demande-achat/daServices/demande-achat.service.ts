import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateDemandeAchatRequest, DemandeAchat,UpdateDemandeAchatRequest} from '../model/demande-achat.model';
import { environment } from '../../../environments/environment';



@Injectable({
  providedIn: 'root'
})
export class DemandeAchatService {
  private apiUrl = `${environment.apiUrl}/demande-achats`;

  constructor(private http: HttpClient) { }

  getAllDemandesAchats(): Observable<DemandeAchat[]> {
    return this.http.get<DemandeAchat[]>(this.apiUrl);
  }

  getDemandeAchatById(id: number): Observable<DemandeAchat> {
    return this.http.get<DemandeAchat>(`${this.apiUrl}/${id}`);
  }

  createDemandeAchat(request: CreateDemandeAchatRequest): Observable<DemandeAchat> {
    return this.http.post<DemandeAchat>(this.apiUrl, request);
  }
  
  updateDemandeAchat(id: number, request: UpdateDemandeAchatRequest): Observable<DemandeAchat> {
    return this.http.put<DemandeAchat>(`${this.apiUrl}/${id}`, request);
  }

  deleteDemandeAchat(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getDAPdf(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/pdf`, { responseType: 'blob' });
  }


  public getApiUrl(): string {
    return this.apiUrl;
  }




}
