import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialogRef } from '@angular/material/dialog';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service';


@Component({
  selector: 'app-demande-achat-details-dialog',
  standalone: false,
  templateUrl: './demande-achat-details-dialog.component.html',
  styleUrl: './demande-achat-details-dialog.component.scss'
})
export class DemandeAchatDetailsDialogComponent {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<DemandeAchatDetailsDialogComponent>,
    public themeService: CustomizerSettingsService
  ) {}

  close() {
    this.dialogRef.close(); // Ferme le dialog
  }

}
