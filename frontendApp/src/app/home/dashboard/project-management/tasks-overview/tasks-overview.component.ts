import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { DashboardService } from '../../service/dashboard.service';
import { ChartComponent, ApexChart, ApexNonAxisChartSeries, ApexPlotOptions, ApexDataLabels } from "ng-apexcharts";
import { ChartData } from '../../../../models/dashboard/kpi.model';
import { DemandeAchatStatus } from '../../../../demande-achat/model/enums.model';


export type ChartOptions = {
    series: ApexNonAxisChartSeries;
    chart: ApexChart;
    labels: string[];
    plotOptions: ApexPlotOptions;
    colors: string[];
    dataLabels: ApexDataLabels;
};

@Component({
  selector: 'app-tasks-overview',
  standalone: false,
  templateUrl: './tasks-overview.component.html',
  styleUrls: ['./tasks-overview.component.scss']
})
export class TasksOverviewComponent implements OnInit {

  private dashboardService = inject(DashboardService);
  
  @ViewChild("chart") chart!: ChartComponent;
  public chartOptions: Partial<ChartOptions>;
  
  isLoading = false;

  constructor() {
    this.chartOptions = {
      series: [],
      chart: { type: "donut", height: 350 },
      labels: [],
      plotOptions: { pie: { donut: { labels: { show: true, total: { show: true, label: 'Total DA' } } } } },
      colors: ["#605DFF", "#AD63F6", "#FD5812", "#D71C00"], // Couleurs pour EN_ATTENTE, APPROUVEE, REFUSEE, ANNULEE
      dataLabels: { enabled: false }
    };
  }

  ngOnInit(): void {
    this.loadDaStatusData();
  }

  loadDaStatusData(): void {
    this.isLoading = true;
    this.dashboardService.getDemandeAchatStatusCounts().subscribe({
      next: (data) => {
        this.updateChart(data);
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  updateChart(data: ChartData<DemandeAchatStatus>[]): void {
    const seriesData = data.map(d => d.value);
    const labelsData = data.map(d => d.label.replace('_', ' ')); // Remplace EN_ATTENTE par En Attente
    
    if (this.chart) {
      this.chart.updateOptions({
        series: seriesData,
        labels: labelsData
      });
    } else {
        // Si le graphique n'est pas encore initialisé
        this.chartOptions.series = seriesData;
        this.chartOptions.labels = labelsData;
    }
  }
}