import { Component, inject, OnInit, ViewChild } from '@angular/core';
import { DemandeAchatService } from '../daServices/demande-achat.service';
import { MatTableDataSource } from '@angular/material/table';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Ticket } from '../../ticket/models/ticket.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatPaginator } from '@angular/material/paginator';
import { forkJoin, Observable } from 'rxjs';
import { Devis } from '../../devis/model/devis.model';
import { TicketService } from '../../ticket/TicketService/ticket.service';
import { DevisService } from '../../devis/devisService/devis.service';
import { CreateDemandeAchatRequest, DemandeAchat, UpdateDemandeAchatRequest } from '../model/demande-achat.model';


@Component({
  selector: 'app-liste-da',
  standalone: false,
  templateUrl: './liste-da.component.html',
  styleUrl: './liste-da.component.scss'
})
export class ListeDaComponent implements OnInit {

  displayedColumns: string[] = ['id', 'statut', 'objet', 'ticketTitre', 'devisId', 'nomInitiateur', 'dateCreationDa', 'action'];
  dataSource = new MatTableDataSource<DemandeAchat>([]);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  isLoading = false;
  isFormVisible = false;
  isEditing = false;
  editingDAId: number | null = null;
  
  daForm: FormGroup;
  
  tickets: Ticket[] = [];
  devisList: Devis[] = [];
  
  public themeService = inject(CustomizerSettingsService);
  private daService = inject(DemandeAchatService);
  private ticketService = inject(TicketService);
  private devisService = inject(DevisService);
  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);

  constructor() {
    this.daForm = this.fb.group({
      lieuLivraison: ['', Validators.required],
      serviceDemandeur: ['', Validators.required],
      objet: ['', Validators.required],
      ticketId: [{value: null, disabled: false}, Validators.required],
      devisId: [{value: null, disabled: false}, Validators.required]
    });
  }

  ngOnInit() {
    this.loadInitialData();
  }

  loadInitialData(): void {
    this.isLoading = true;
    forkJoin({
      demandes: this.daService.getAllDemandesAchats(),
      tickets: this.ticketService.getTickets(),
      devis: this.devisService.getAllDevis()
    }).subscribe({
      next: ({ demandes, tickets, devis }) => {
        this.dataSource.data = demandes;
        this.dataSource.paginator = this.paginator;
        this.tickets = tickets;
        this.devisList = devis;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        console.error("Erreur de chargement des données pour les DA", err);
      }
    });
  }
  
  refreshDemandesList(): void {
    this.isLoading = true;
    this.daService.getAllDemandesAchats().subscribe({
      next: (demandes) => {
        this.dataSource.data = demandes;
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  openForm(da?: DemandeAchat): void {
    this.isFormVisible = true;
    if (da) {
      this.isEditing = true;
      this.editingDAId = da.id;
      // Pour la modification, on ne peut changer que certains champs.
      // Le ticket et le devis sont verrouillés.
      this.daForm.patchValue({
        lieuLivraison: da.lieuLivraison,
        serviceDemandeur: da.serviceDemandeur,
        objet: da.objet,
        ticketId: da.ticketId,
        devisId: da.devisId
      });
      // Désactiver les selects en mode édition
      this.daForm.get('ticketId')?.disable();
      this.daForm.get('devisId')?.disable();
    } else {
      this.isEditing = false;
      this.editingDAId = null;
      this.daForm.reset();
      // Activer les selects en mode création
      this.daForm.get('ticketId')?.enable();
      this.daForm.get('devisId')?.enable();
    }
  }

  closeForm(): void {
    this.isFormVisible = false;
  }

  onSubmit(): void {
    if (this.daForm.invalid) {
      this.snackBar.open('Veuillez remplir tous les champs obligatoires.', 'Fermer', { duration: 3000 });
      return;
    }

    this.isLoading = true;
    
    let request$: Observable<DemandeAchat>;

    if (this.isEditing && this.editingDAId) {
        const updateRequest: UpdateDemandeAchatRequest = {
            lieuLivraison: this.daForm.value.lieuLivraison,
            serviceDemandeur: this.daForm.value.serviceDemandeur,
            objet: this.daForm.value.objet
        };
        request$ = this.daService.updateDemandeAchat(this.editingDAId, updateRequest);
    } else {
        const createRequest: CreateDemandeAchatRequest = this.daForm.value;
        request$ = this.daService.createDemandeAchat(createRequest);
    }
    
    request$.subscribe({
      next: () => {
        const message = this.isEditing ? 'Demande d\'achat modifiée avec succès !' : 'Demande d\'achat créée avec succès !';
        this.snackBar.open(message, 'OK', { duration: 3000 });
        this.closeForm();
        this.refreshDemandesList();
      },
      error: () => this.isLoading = false
    });
  }
  
  onDelete(id: number): void {
    if (confirm("Êtes-vous sûr de vouloir supprimer cette demande d'achat ?")) {
      this.isLoading = true;
      this.daService.deleteDemandeAchat(id).subscribe({
        next: () => {
          this.snackBar.open('Demande d\'achat supprimée avec succès !', 'OK', { duration: 3000 });
          this.refreshDemandesList();
        },
        error: () => this.isLoading = false
      });
    }
  }
  
  onPrint(id: number): void {
      this.daService.getDAPdf(id).subscribe(blob => {
          const fileURL = URL.createObjectURL(blob);
          window.open(fileURL, '_blank');
      });
  }
}
