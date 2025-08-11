import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ticket, CreateTicketRequest, UpdateTicketRequest } from '../models/ticket.model';
import { environment } from '../../../../src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class TicketService {
  // CORRIGÉ : L'URL pointe vers l'endpoint RESTful
  private apiUrl = `${environment.apiUrl}/tickets`;

  constructor(private http: HttpClient) { }

  getTickets(): Observable<Ticket[]> {
    return this.http.get<Ticket[]>(this.apiUrl);
  }


  getTicketById(id: number): Observable<Ticket> {
    return this.http.get<Ticket>(`${this.apiUrl}/${id}`);
  }
  
  // CORRIGÉ : Utilise le DTO de requête
  createTicket(ticketRequest: CreateTicketRequest): Observable<Ticket> {
    return this.http.post<Ticket>(this.apiUrl, ticketRequest);
  }

  // CORRIGÉ : Utilise le DTO de requête
  updateTicket(id: number, ticketRequest: UpdateTicketRequest): Observable<Ticket> {
    return this.http.put<Ticket>(`${this.apiUrl}/${id}`, ticketRequest);
  }

  deleteTicket(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // NOUVEAU : Service pour imprimer le PDF
  getTicketPdf(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/pdf`, { responseType: 'blob' });
  }
  
}