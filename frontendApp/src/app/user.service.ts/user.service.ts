import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../ticket/models/user.model'; // Assurez-vous que le modèle est correct
import { environment } from '../../../src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) { }

  /**
   * Récupère la liste complète des utilisateurs.
   */
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }
  // Vous pouvez ajouter le reste du CRUD pour les utilisateurs ici plus tard
}