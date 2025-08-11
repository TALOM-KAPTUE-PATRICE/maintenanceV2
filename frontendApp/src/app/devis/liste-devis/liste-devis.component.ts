// Fichier : src/app/devis/liste-devis/liste-devis.component.ts
import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { forkJoin, Observable } from 'rxjs';

import { DevisService } from '../devisService/devis.service';
import { ClientService } from '../../client/client.service';
import { Devis, CreateDevisRequest, UpdateDevisRequest } from '../model/devis.model';
import { DevisDetailsComponent } from '../devis-details/devis-details.component';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service';
import { Client } from '../../ticket/models/client.model';
import { DevisStatus } from '../model/enums.model';
import { ArticleComponent } from '../article/article.component';

@Component({
  selector: 'app-liste-devis',
  standalone: false,
  templateUrl: './liste-devis.component.html',
  styleUrls: ['./liste-devis.component.scss']
})
export class ListeDevisComponent implements OnInit {

  displayedColumns: string[] = ['id', 'statut', 'description', 'clientNom', 'dateCreation', 'dateValidite', 'action'];
  dataSource = new MatTableDataSource<Devis>([]);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  isLoading = false;
  isFormVisible = false;
  isEditing = false;
  editingDevisId: string | null = null;
  
  devisForm: FormGroup;
  clients: Client[] = [];
  devisStatutOptions = Object.values(DevisStatus); // Pour le select de statut

  public themeService = inject(CustomizerSettingsService);
  private devisService = inject(DevisService);
  private clientService = inject(ClientService);
  private fb = inject(FormBuilder);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  constructor() {
    this.devisForm = this.fb.group({
      description: ['', Validators.required],
      dateValidite: [null, Validators.required],
      clientId: [null, Validators.required],
      typeTravaux: [''],
      contrainte: [''],
      peinture: [false],
      effectif: [1, [Validators.required, Validators.min(1)]],
      livraison: [''],
      devise: ['XAF', Validators.required],
      siteIntervention: ['']
    });
  }

  ngOnInit() {
    this.loadInitialData();
  }

  loadInitialData(): void {
    this.isLoading = true;
    forkJoin({
      devis: this.devisService.getAllDevis(),
      clients: this.clientService.getAllClients()
    }).subscribe({
      next: ({ devis, clients }) => {
        this.dataSource.data = devis;
        this.dataSource.paginator = this.paginator;
        this.clients = clients;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        console.error("Erreur de chargement des données pour les devis", err);
      }
    });
  }

  refreshDevisList(): void {
    this.isLoading = true;
    this.devisService.getAllDevis().subscribe({
      next: (devis) => {
        this.dataSource.data = devis;
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }
  
  openForm(devis?: Devis): void {
    this.isFormVisible = true;
    if (devis) {
      this.isEditing = true;
      this.editingDevisId = devis.id;
            // Pour la date, il faut la convertir en objet Date pour que le datepicker la reconnaisse
      const devisDataForForm = { ...devis, dateValidite: new Date(devis.dateValidite) };
      this.devisForm.patchValue({devisDataForForm});
    } else {
      this.isEditing = false;
      this.editingDevisId = null;
      this.devisForm.reset({ peinture: false, effectif: 1, devise: 'XAF' });
    }
  }
  
  closeForm(): void {
    this.isFormVisible = false;
  }
  
  onSubmit(): void {
    if (this.devisForm.invalid) {
      this.snackBar.open('Veuillez corriger les erreurs dans le formulaire.', 'Fermer', { duration: 3000 });
      return;
    }
    this.isLoading = true;
    
    // Formater la date
    const formValue = this.devisForm.value;
    const date = new Date(formValue.dateValidite);
        // Gestion du fuseau horaire pour éviter les décalages de date
    date.setMinutes(date.getMinutes() - date.getTimezoneOffset());
    const formattedDate = date.toISOString().split('T')[0]; // Format AAAA-MM-JJ
    
    let request$: Observable<Devis>;

    if (this.isEditing && this.editingDevisId) {
        const updateRequest: UpdateDevisRequest = { ...formValue, dateValidite: formattedDate };
        request$ = this.devisService.updateDevis(this.editingDevisId, updateRequest);
    } else {
        const createRequest: CreateDevisRequest = { ...formValue, dateValidite: formattedDate };
        request$ = this.devisService.createDevis(createRequest);
    }

    request$.subscribe({
        next: () => {
            const message = this.isEditing ? 'Devis modifié avec succès !' : 'Devis créé avec succès !';
            this.snackBar.open(message, 'OK', { duration: 3000, panelClass: ['success-snackbar'] });
            this.closeForm();
            this.refreshDevisList();
        },
        error: () => this.isLoading = false
    });
  }

    // C'est cette méthode qui ouvre le dialogue pour gérer les articles
  onManageArticles(devisId: string): void {
    const dialogRef = this.dialog.open(ArticleComponent, {
      data: { devisId: devisId },
      width: '90%',
      maxWidth: '1200px',
      height: '90vh'
    });

    dialogRef.afterClosed().subscribe(() => {
      // Optionnel : rafraîchir la liste des devis si on veut mettre à jour un montant total par exemple
      console.log('Dialogue des articles fermé.');
    });
  }


  onDelete(id: string): void {
    if (confirm("Êtes-vous sûr de vouloir supprimer ce devis ? Cette action est irréversible.")) {
      this.isLoading = true;
      this.devisService.deleteDevis(id).subscribe({
        next: () => {
          this.snackBar.open('Devis supprimé avec succès !', 'OK', { duration: 3000 });
          this.refreshDevisList();
        },
        error: () => this.isLoading = false
      });
    }
  }

  onViewDetails(devisId: string): void {
    // On recharge les données complètes pour avoir la liste des articles à jour
    this.devisService.getDevisById(devisId).subscribe(fullDevis => {
        this.dialog.open(DevisDetailsComponent, {
            data: fullDevis,
            width: '700px',
            panelClass: 'details-dialog-container'
        });
    });
  }


  onPrint(id: string): void {
      this.devisService.printDevis(id).subscribe(blob => {
          const fileURL = URL.createObjectURL(blob);
          window.open(fileURL, '_blank');
      });
  }
  
 
}