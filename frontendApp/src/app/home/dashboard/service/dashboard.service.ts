import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProjectStatusKpi, ChartData } from '../../../models/dashboard/kpi.model';
import { environment } from '../../../../environments/environment';
import { DevisStatus } from '../../../devis/model/enums.model';
import { DemandeAchatStatus } from '../../../demande-achat/model/enums.model';



@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private apiUrl = `${environment.apiUrl}/dashboard`;
  private http = inject(HttpClient);

  // Pour KPI 1
  getProjectStatus(): Observable<ProjectStatusKpi[]> {
    return this.http.get<ProjectStatusKpi[]>(`${this.apiUrl}/project-status`);
  }

  // Pour KPI 2
  getSubmittedDevisByClient(): Observable<ChartData<string>[]> {
    return this.http.get<ChartData<string>[]>(`${this.apiUrl}/devis-by-client`);
  }



    /**
   * Pour KPI 3 : Récupère le compte des devis par statut (Validé, Refusé, etc.)
   */
  getDevisStatusCounts(): Observable<ChartData<DevisStatus>[]> {
    return this.http.get<ChartData<DevisStatus>[]>(`${this.apiUrl}/devis-status`);
  }

  /**
   * Pour KPI 4 : Récupère le compte des projets avec ou sans bon de commande client.
   */
  getProjectsWithOrWithoutClientPO(): Observable<ChartData<string>[]> {
    return this.http.get<ChartData<string>[]>(`${this.apiUrl}/projects-po-status`);
  }
  
  /**
   * Pour KPI 5 : Récupère le compte des demandes d'achat par statut.
   */

  getDemandeAchatStatusCounts(): Observable<ChartData<DemandeAchatStatus>[]> {
    return this.http.get<ChartData<DemandeAchatStatus>[]>(`${this.apiUrl}/demande-achat-status`);
  }
}