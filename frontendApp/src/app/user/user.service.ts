import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { User, CreateUserRequest, UpdateUserRequest } from './models/user.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;
  private http = inject(HttpClient);

  getAll(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  getById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  create(request: CreateUserRequest, profileImage?: File): Observable<User> {
    const formData = new FormData();
    formData.append('user', new Blob([JSON.stringify(request)], { type: 'application/json' }));
    if (profileImage) {
      formData.append('file', profileImage, profileImage.name);
    }
    return this.http.post<User>(this.apiUrl, formData);
  }

  update(id: number, request: UpdateUserRequest, profileImage?: File): Observable<User> {
    const formData = new FormData();
    formData.append('user', new Blob([JSON.stringify(request)], { type: 'application/json' }));
    if (profileImage) {
      formData.append('file', profileImage, profileImage.name);
    }
    return this.http.put<User>(`${this.apiUrl}/${id}`, formData);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}