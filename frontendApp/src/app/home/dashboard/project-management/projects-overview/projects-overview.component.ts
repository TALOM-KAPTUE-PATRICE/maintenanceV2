import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { CustomizerSettingsService } from '../../customizer-settings/customizer-settings.service';
import { DashboardService } from '../../service/dashboard.service';
import { ApexOptions } from 'apexcharts';
import { DevisStatus } from '../../../../devis/model/enums.model';
import { ChartData } from '../../../../models/dashboard/kpi.model';

// ▼▼▼ IMPORTATIONS CORRIGÉES ET NÉCESSAIRES ▼▼▼
import {
    ChartComponent,
    ApexAxisChartSeries,
    ApexChart,
    ApexXAxis,
    ApexDataLabels,
    ApexPlotOptions,
    ApexTitleSubtitle
} from "ng-apexcharts";
import { forkJoin } from 'rxjs';

// Créer un type pour les options du graphique pour plus de clarté
export type ChartOptions = {
    series: ApexAxisChartSeries;
    chart: ApexChart;
    xaxis: ApexXAxis;
    plotOptions: ApexPlotOptions;
    dataLabels: ApexDataLabels;
    title: ApexTitleSubtitle;
    colors: string[];
};


@Component({
  selector: 'app-projects-overview',
  standalone: false,
  templateUrl: './projects-overview.component.html',
  styleUrls: ['./projects-overview.component.scss']
})
export class ProjectsOverviewComponent implements OnInit {

  public themeService = inject(CustomizerSettingsService);
  private dashboardService = inject(DashboardService);

  // Pour KPI 3 (Devis validés vs non validés)
  validatedCount: number = 0;
  rejectedCount: number = 0;
  submittedCount: number = 0;

  // Pour KPI 2 (Graphique)
  @ViewChild("chart") chart: ChartComponent | undefined;
  // ▼▼▼ TYPAGE EXPLICITE ET COMPLET DES OPTIONS ▼▼▼
  public chartOptions: Partial<ChartOptions>;
  isLoading = false;


    // Pour KPI 3
  validatedDevisCount: number = 0;
  rejectedDevisCount: number = 0;
  
  // Pour KPI 4
  projectsWithPO: number = 0;
  projectsWithoutPO: number = 0;

  constructor() {
    this.chartOptions = {
      series: [],
      chart: {
        type: 'bar',
        height: 350,
        toolbar: { show: false }
      },
      plotOptions: { bar: { horizontal: true, barHeight: '60%' } },
      dataLabels: { enabled: false },
      xaxis: { categories: [] },
      colors: ['#3f51b5'],
      title: { // Ajouté pour être complet, même si vide
        text: undefined
      }
    };
  }

  ngOnInit(): void {
    // this.loadKpiData();
    this.loadKpiDatas();
  }

  loadKpiData(): void {
    this.isLoading = true;
    this.dashboardService.getDevisStatusCounts().subscribe(data => {
      this.validatedCount = data.find(d => d.label === DevisStatus.VALIDER)?.value || 0;
      this.rejectedCount = data.find(d => d.label === DevisStatus.REFUSER)?.value || 0;
      this.submittedCount = data.find(d => d.label === DevisStatus.SOUMIS)?.value || 0;
    });

    this.dashboardService.getSubmittedDevisByClient().subscribe(data => {
        this.updateChart(data);
        this.isLoading = false;
    });
  }

   loadKpiDatas(): void {
    this.isLoading = true;
    forkJoin({
      devisStatus: this.dashboardService.getDevisStatusCounts(),
      poStatus: this.dashboardService.getProjectsWithOrWithoutClientPO()
    }).subscribe({
      next: ({ devisStatus, poStatus }) => {

        
        // KPI 3
        this.validatedDevisCount = devisStatus.find(d => d.label === DevisStatus.VALIDER)?.value || 0;
        this.rejectedDevisCount = devisStatus.find(d => d.label === DevisStatus.REFUSER)?.value || 0;

        // KPI 4
        this.projectsWithPO = poStatus.find(p => p.label === "Avec Bon de Commande Client")?.value || 0;
        this.projectsWithoutPO = poStatus.find(p => p.label === "Sans Bon de Commande Client")?.value || 0;

        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }



  updateChart(data: ChartData<string>[]): void {
    const seriesData = data.map(d => d.value);
    const categoriesData = data.map(d => d.label);
    
    // Utiliser updateOptions pour une mise à jour réactive du graphique
    if (this.chart) {
        this.chart.updateOptions({
            series: [{ name: 'Devis Déposés', data: seriesData }],
            xaxis: { categories: categoriesData }
        });
    }
  }
}