import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { forkJoin } from 'rxjs';

import { BonCommandeClientService } from '../bon-commande-client.service';
import { DevisService } from '../../devis/devisService/devis.service';
import { BonCommandeClient } from '../models/bon-commande-client.model';
import { Devis } from '../../devis/model/devis.model';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-bon-commande-client-list',
  standalone: false,
  templateUrl: './bon-commande-client-list.component.html',
  styleUrls: ['./bon-commande-client-list.component.scss']
})
export class BonCommandeClientListComponent implements OnInit {

  private bccService = inject(BonCommandeClientService);
  private devisService = inject(DevisService);
  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);
  public themeService = inject(CustomizerSettingsService);

  displayedColumns: string[] = ['referenceClient', 'dateReception', 'clientNom', 'devisId', 'action'];
  dataSource = new MatTableDataSource<BonCommandeClient>();
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  isLoading = false;
  isFormVisible = false;
  bccForm: FormGroup;
  devisList: Devis[] = [];
  selectedFile: File | null = null;
  
  private apiUrl = `${environment.apiUrl.replace('/api', '')}`;
  
  constructor() {
    this.bccForm = this.fb.group({
      devisId: [null, Validators.required],
      referenceClient: ['', Validators.required],
      dateReception: [new Date(), Validators.required],
      notes: [''],
      pdfFile: [null, Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadInitialData();
  }

  loadInitialData(): void {
    this.isLoading = true;
    forkJoin({
      bccs: this.bccService.getAll(),
      devis: this.devisService.getAllDevis()
    }).subscribe({
      next: ({ bccs, devis }) => {
        this.dataSource.data = bccs;
        this.dataSource.paginator = this.paginator;
        this.devisList = devis; // Idéalement, filtrer pour n'afficher que les devis 'SOUMIS'
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  refreshList(): void {
    this.isLoading = true;
    this.bccService.getAll().subscribe({
      next: (data) => {
        this.dataSource.data = data;
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    this.dataSource.paginator?.firstPage();
  }

  openForm(): void {
    this.isFormVisible = true;
    this.bccForm.reset({ dateReception: new Date() });
    this.selectedFile = null;
  }

  closeForm(): void {
    this.isFormVisible = false;
  }

  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      this.selectedFile = file;
      this.bccForm.patchValue({ pdfFile: file });
      this.bccForm.get('pdfFile')?.updateValueAndValidity();
    }
  }

  onSubmit(): void {
    if (this.bccForm.invalid) {
      this.snackBar.open('Veuillez remplir tous les champs et sélectionner un fichier PDF.', 'Fermer', { duration: 4000 });
      return;
    }

    this.isLoading = true;
    
    // Préparer la requête sans le fichier
    const requestData = { ...this.bccForm.value };
    delete requestData.pdfFile;
    
    this.bccService.create(requestData, this.selectedFile!).subscribe({
      next: () => {
        this.snackBar.open('Bon de commande client enregistré avec succès !', 'OK', { duration: 3000 });
        this.closeForm();
        this.refreshList();
      },
      error: () => this.isLoading = false
    });
  }

  // ▼▼▼ AJOUTEZ CETTE MÉTHODE COMPLÈTE ▼▼▼
  onDelete(id: number): void {
    // Utiliser une boîte de dialogue de confirmation standard du navigateur
    if (confirm("Êtes-vous sûr de vouloir supprimer ce bon de commande client ?\nCette action est irréversible.")) {
      this.isLoading = true; // Activer l'indicateur de chargement
      this.bccService.delete(id).subscribe({
        next: () => {
          this.snackBar.open('Bon de commande supprimé avec succès !', 'OK', { 
            duration: 3000,
            panelClass: 'success-snackbar' 
          });
          this.refreshList(); // Rafraîchir la liste pour refléter la suppression
        },
        error: (err) => {
          // L'erreur est déjà affichée par l'intercepteur, on arrête juste le chargement
          this.isLoading = false;
          console.error("Erreur lors de la suppression :", err);
        }
      });
    }
  }


  viewPdf(pdfUrl?: string): void {
    if (pdfUrl && pdfUrl.trim()) {
      // Le pdfUrl retourné par le backend est déjà une URL complète et cliquable.
      // (ex: http://localhost:8081/b_commandes_client/nom-fichier.pdf)
      // Il suffit de l'ouvrir.
      window.open(pdfUrl, '_blank');
    } else {
      this.snackBar.open('Aucun fichier PDF associé à ce bon de commande.', 'Fermer', { duration: 3000 });
    }
  }

}