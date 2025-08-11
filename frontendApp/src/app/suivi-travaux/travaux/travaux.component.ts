import { Component, OnInit, ViewChild } from '@angular/core';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';


export interface DevisData {
  site: string;
  nombreDevis: number;
}

const ELEMENT_DATA: DevisData[] = [
  { site: 'Ndokoti', nombreDevis: 45 },
  { site: 'Socaver', nombreDevis: 30 },
  { site: 'Koumassi', nombreDevis: 20 },
  { site: 'Cimencam', nombreDevis: 15 }
];

export interface Projet {
  nom: string;
  statut: string;
  progression: number;
}

const PROJETS_DATA: Projet[] = [
  { nom: 'Projet A', statut: 'En cours', progression: 45 },
  { nom: 'Projet B', statut: 'Terminé', progression: 100 },
  { nom: 'Projet C', statut: 'En arrêt', progression: 25 }
];



@Component({
  selector: 'app-travaux',
  standalone: false,
  templateUrl: './travaux.component.html',
  styleUrl: './travaux.component.scss'
})

export class TravauxComponent implements OnInit {

  constructor(public themeService: CustomizerSettingsService) {

  }

  devisNonValides: number = 12;
  devisValides: number = 38;
  daAnnules: number = 5;

  daEnAttente = [
    { nom: 'DA-001', statut: 'En attente de validation' },
    { nom: 'DA-002', statut: 'En attente de budget' },
    { nom: 'DA-003', statut: 'En attente de fournisseur' }
  ];

  displayedColumns: string[] = ['site', 'nombreDevis'];
  // dataSource = new MatTableDataSource(ELEMENT_DATA);

  displayedColumnsp: string[] = ['nom', 'statut', 'progression'];
  dataSource = new MatTableDataSource(PROJETS_DATA);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }


}
