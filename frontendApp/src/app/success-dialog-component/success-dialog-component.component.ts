import { Component, Inject, OnInit, OnDestroy } from '@angular/core'; // Ajout de OnInit, OnDestroy
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button'; // Importer MatButtonModule
import { CommonModule } from '@angular/common'; // Importer CommonModule pour *ngIf etc.

// Définir une interface plus précise pour les données passées au dialogue
export interface SuccessDialogData {
  title?: string; // Titre optionnel
  message: string;
  autoCloseDelay?: number; // Délai en millisecondes pour la fermeture automatique
}

@Component({
  selector: 'app-success-dialog-component',
  standalone: true, // Rendre le composant standalone pour simplifier les imports
  imports: [
    CommonModule, // Nécessaire pour *ngIf, *ngFor, etc. si utilisé dans le template
    MatDialogModule,
    MatIconModule,
    MatButtonModule // Nécessaire pour mat-button, mat-flat-button
  ],
  templateUrl: './success-dialog-component.component.html',
  styleUrls: ['./success-dialog-component.component.scss'] // Changé en styleUrls
})
export class SuccessDialogComponentComponent implements OnInit, OnDestroy {
  public dialogTitle: string;
  public autoCloseSeconds: number = 0;
  private timerId: any;

  constructor(
    public dialogRef: MatDialogRef<SuccessDialogComponentComponent>,
    @Inject(MAT_DIALOG_DATA) public data: SuccessDialogData // Utiliser l'interface
  ) {
    this.dialogTitle = data.title || 'Opération Réussie'; // Titre par défaut
  }

  ngOnInit(): void {
    if (this.data.autoCloseDelay && this.data.autoCloseDelay > 0) {
      this.autoCloseSeconds = Math.floor(this.data.autoCloseDelay / 1000);
      this.timerId = setInterval(() => {
        this.autoCloseSeconds--;
        if (this.autoCloseSeconds <= 0) {
          this.onClose();
        }
      }, 1000);

      // Fermer aussi après le délai total pour plus de précision
      setTimeout(() => {
        this.onClose();
      }, this.data.autoCloseDelay);
    }
  }

  onClose(): void {
    this.dialogRef.close();
  }

  ngOnDestroy(): void {
    if (this.timerId) {
      clearInterval(this.timerId);
    }
  }
  
}