import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client } from '../ticket/models/client.model'; // Assurez-vous que le modèle est correct
import { environment } from '../../../src/environments/environment';
import { Page } from '../ticket/models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private apiUrl = `${environment.apiUrl}/clients`;

  constructor(private http: HttpClient) { }

  /**
   * Récupère la liste complète des clients.
   */
  getAllClients(): Observable<Client[]> {
    return this.http.get<Client[]>(this.apiUrl);
  }
  
}