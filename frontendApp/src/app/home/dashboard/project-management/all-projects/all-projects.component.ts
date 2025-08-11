import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { CustomizerSettingsService } from '../../customizer-settings/customizer-settings.service';
import { DashboardService } from '../../service/dashboard.service';
import { ProjectStatusKpi } from '../../../../models/dashboard/kpi.model';


@Component({
  selector: 'app-all-projects',
  standalone: false,
  templateUrl: './all-projects.component.html',
  styleUrls: ['./all-projects.component.scss']
})
export class AllProjectsComponent implements OnInit {

  public themeService = inject(CustomizerSettingsService);
  private dashboardService = inject(DashboardService);
  
  displayedColumns: string[] = ['titre', 'clientNom', 'statut', 'avancement'];
  dataSource = new MatTableDataSource<ProjectStatusKpi>();
  
  
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  isLoading = false;

  ngOnInit(): void {
    this.loadProjectStatus();
  }

  loadProjectStatus(): void {
    this.isLoading = true;
    this.dashboardService.getProjectStatus().subscribe({
      next: (data) => {
        this.dataSource.data = data;
        this.dataSource.paginator = this.paginator;
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

}