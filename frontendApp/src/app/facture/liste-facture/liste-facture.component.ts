import { Component, inject, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { FactureService } from '../facturesService/facture.service';
import { DevisService } from '../../devis/devisService/devis.service';
import { ServiceAppService } from '../../servicesApp/service-app.service';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service';
import { FactureDetailsComponent } from '../facture-details/facture-details.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatPaginator } from '@angular/material/paginator';
import { SuccessDialogComponentComponent } from '../../success-dialog-component/success-dialog-component.component';
import { Facture } from '../models/facture.model';
import { forkJoin } from 'rxjs';
import { Client } from '../../ticket/models/client.model';
import { Devis } from '../../devis/model/devis.model';
import { ClientService } from '../../client/client.service';


@Component({
  selector: 'app-liste-facture',
  standalone: false,
  templateUrl: './liste-facture.component.html',
  styleUrl: './liste-facture.component.scss'
})
export class ListeFactureComponent implements OnInit {
  // Injections
  public themeService = inject(CustomizerSettingsService);
  private fb = inject(FormBuilder);
  private factureService = inject(FactureService);
  private devisService = inject(DevisService);
  private clientService = inject(ClientService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);
  
  // Table
  displayedColumns: string[] = ['id', 'dateFacturation', 'clientNom', 'montant','statut','devisId', 'action'];
  dataSource = new MatTableDataSource<Facture>();
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  // State
  isLoading = false;
  isFormVisible = false;
  isEditing = false;
  editingFactureId: string | null = null;

  // Form
  factureForm: FormGroup;
  
  // Data for Selects

  devisList: Devis[] = [];
  
  clientList: Client[] = [];

  constructor() {
    this.factureForm = this.fb.group({
      devisId: [null, Validators.required],
      clientId: [null, Validators.required],
      numeroSecretariat: ['', Validators.required],
      emailBE: ['', [Validators.email]],
      numBetang: [''],
    });
  }

  ngOnInit(): void {
    this.loadInitialData();
  }

  loadInitialData(): void {
    this.isLoading = true;
    forkJoin({
      factures: this.factureService.getAll(),
      devis: this.devisService.getAllDevis(),
      clients: this.clientService.getAllClients()
    }).subscribe({
      next: ({ factures, devis, clients }) => {
        this.dataSource.data = factures;
        this.dataSource.paginator = this.paginator;
        this.devisList = devis;
        this.clientList = clients;
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  refreshList(): void {
    this.isLoading = true;
    this.factureService.getAll().subscribe({
      next: (data) => {
        this.dataSource.data = data;
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    this.dataSource.paginator?.firstPage();
  }
  
  openForm(facture?: Facture): void {
    this.isFormVisible = true;
    if (facture) {
      this.isEditing = true;
      this.editingFactureId = facture.id;
      this.factureService.getById(facture.id).subscribe(fullFacture => {
        this.factureForm.patchValue({
          numeroSecretariat: fullFacture.numeroSecretariat,
          //... Mappez les autres champs si nécessaire
        });
        // Pour la modification, on désactive les selects client/devis car ils ne doivent pas changer
        this.factureForm.get('clientId')?.disable();
        this.factureForm.get('devisId')?.disable();
      });
    } else {
      this.isEditing = false;
      this.editingFactureId = null;
      this.factureForm.reset();
      this.factureForm.get('clientId')?.enable();
      this.factureForm.get('devisId')?.enable();
    }
  }

  closeForm(): void {
    this.isFormVisible = false;
  }

  onSubmit(): void {
    if (this.factureForm.invalid) {
      this.snackBar.open('Veuillez remplir tous les champs obligatoires.', 'Fermer', { duration: 3000 });
      return;
    }

    this.isLoading = true;
    const request$ = this.isEditing && this.editingFactureId
      ? this.factureService.update(this.editingFactureId, this.factureForm.value)
      : this.factureService.create(this.factureForm.value);

    request$.subscribe({
      next: () => {
        const message = this.isEditing ? 'Facture modifiée avec succès !' : 'Facture créée avec succès !';
        this.snackBar.open(message, 'OK', { duration: 3000 });
        this.closeForm();
        this.refreshList();
      },
      error: () => this.isLoading = false
    });
  }

  onDelete(id: string): void {
    if (confirm("Êtes-vous sûr de vouloir supprimer cette facture ?")) {
      this.factureService.delete(id).subscribe({
        next: () => {
          this.snackBar.open('Facture supprimée avec succès !', 'OK', { duration: 3000 });
          this.refreshList();
        }
      });
    }
  }

  onPrint(id: string): void {
    this.factureService.printPdf(id).subscribe(blob => {
      const fileURL = URL.createObjectURL(blob);
      window.open(fileURL, '_blank');
    });
  }

  onViewDetails(facture: Facture): void {
    this.dialog.open(FactureDetailsComponent, {
      data: facture,
      width: '600px'
    });
  }
}
