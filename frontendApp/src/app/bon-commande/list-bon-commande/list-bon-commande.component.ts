import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { forkJoin } from 'rxjs';
import { BonCommandeFournisseurService } from '../bon-commandeService/bon-commande.service';
import { DevisService } from '../../devis/devisService/devis.service';
import { FournisseurService } from '../../fournisseur/fournisseur.service';
import { BonCommandeFournisseur } from '../model/bon-commande-fournisseur.model';
import { Devis } from '../../devis/model/devis.model';
import { Fournisseur } from '../../fournisseur/models/fournisseur.model'; // Assurez-vous d'avoir ce modèle
import { BonCommandeDetailsComponent } from '../bon-commande-details/bon-commande-details.component';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service';

@Component({
  selector: 'app-list-bon-commande',
  standalone: false,
  templateUrl: './list-bon-commande.component.html',
  styleUrls: ['./list-bon-commande.component.scss']
})
export class ListBonCommandeComponent implements OnInit {
  
  // Injections
  private bcfService = inject(BonCommandeFournisseurService);
  private devisService = inject(DevisService);
  public themeService = inject(CustomizerSettingsService);
  private fournisseurService = inject(FournisseurService);
  private fb = inject(FormBuilder);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  // Table
  displayedColumns: string[] = ['id', 'dateCommande', 'fournisseurEmail', 'devisId','statut','action'];
  dataSource = new MatTableDataSource<BonCommandeFournisseur>();
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  
  // State Management
  isLoading = false;

  // Form
  bcfForm: FormGroup;
  isFormVisible = false;
  isEditing = false;
  editingBcfId: string | null = null;
  
  // Data for Selects
  devisList: Devis[] = [];
  fournisseurList: Fournisseur[] = [];

  constructor() {
    this.bcfForm = this.fb.group({
      devisId: [null, Validators.required],
      fournisseurId: [null, Validators.required],
      adresseLivraison: ['', [Validators.required, Validators.maxLength(255)]],
      codeProjet: [''],
      codeDevise: ['XAF', Validators.required],
      modeExpedition: [''],
      modePaiement: [''],
      moyenPaiement: [''],
      
    });
  }

  ngOnInit(): void {
    this.loadInitialData();
  }

  loadInitialData(): void {
    this.isLoading = true;
    forkJoin({
      bonCommandes: this.bcfService.getAll(),
      devis: this.devisService.getAllDevis(),
      fournisseurs: this.fournisseurService.getAll()
    }).subscribe({
      next: ({ bonCommandes, devis, fournisseurs }) => {
        this.dataSource.data = bonCommandes;
        this.dataSource.paginator = this.paginator;
        this.devisList = devis;
        this.fournisseurList = fournisseurs;
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  refreshList(): void {
    this.isLoading = true;
    this.bcfService.getAll().subscribe({
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

  openForm(bcf?: BonCommandeFournisseur): void {
    this.isFormVisible = true;
    if (bcf) {
      this.isEditing = true;
      this.editingBcfId = bcf.id;
      // Pour la modif, on doit charger les détails pour pré-remplir
      this.bcfService.getById(bcf.id).subscribe(fullBcf => {
        // Le formulaire d'update n'a pas tous les champs, on patch ce qui est modifiable
        this.bcfForm.patchValue({
          adresseLivraison: fullBcf.adresseLivraison,
          //... map other editable fields from UpdateBcfRequest
        });
        // On désactive les champs qu'on ne peut pas modifier
        this.bcfForm.get('devisId')?.disable();
        this.bcfForm.get('fournisseurId')?.disable();
      });
    } else {
      this.isEditing = false;
      this.editingBcfId = null;
      this.bcfForm.get('devisId')?.enable();
      this.bcfForm.get('fournisseurId')?.enable();
      this.bcfForm.reset({ codeDevise: 'XAF' });
    }
  }

  closeForm(): void {
    this.isFormVisible = false;
  }

  onSubmit(): void {
    if (this.bcfForm.invalid) {
      this.snackBar.open('Veuillez remplir tous les champs obligatoires.', 'Fermer', { duration: 3000 });
      return;
    }

    this.isLoading = true;
    const formValue = this.bcfForm.getRawValue(); // Utilise getRawValue pour inclure les champs désactivés
    const request$ = this.isEditing && this.editingBcfId
      ? this.bcfService.update(this.editingBcfId, formValue)
      : this.bcfService.create(this.bcfForm.value);

    request$.subscribe({
      next: () => {
        const message = this.isEditing ? 'Bon de commande modifié avec succès !' : 'Bon de commande créé avec succès !';
        this.snackBar.open(message, 'OK', { duration: 3000 });
        this.closeForm();
        this.refreshList();
      },
      error: () => this.isLoading = false
    });
  }

  onDelete(id: string): void {
    if (confirm("Êtes-vous sûr de vouloir supprimer ce bon de commande ?")) {
      this.bcfService.delete(id).subscribe({
        next: () => {
          this.snackBar.open('Bon de commande supprimé avec succès !', 'OK', { duration: 3000 });
          this.refreshList();
        }
      });
    }
  }
  
  onPrint(id: string): void {
    this.bcfService.printPdf(id).subscribe(blob => {
      const fileURL = URL.createObjectURL(blob);
      window.open(fileURL, '_blank');
    });
  }

  onViewDetails(bcf: BonCommandeFournisseur): void {
    this.dialog.open(BonCommandeDetailsComponent, {
      data: bcf,
      width: '600px',
      panelClass: 'custom-dialog-container'
    });
  }
}