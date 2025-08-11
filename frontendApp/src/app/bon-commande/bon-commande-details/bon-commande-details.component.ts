// bon-commande-details.component.ts
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { BonCommandeFournisseur } from '../model/bon-commande-fournisseur.model';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service';
 

@Component({
  selector: 'app-bon-commande-details',
  standalone: false,
  templateUrl: './bon-commande-details.component.html',
  styleUrl: './bon-commande-details.component.scss'
})
export class BonCommandeDetailsComponent {
   constructor(@Inject(MAT_DIALOG_DATA) public data: BonCommandeFournisseur,
                  private dialogRef: MatDialogRef<BonCommandeDetailsComponent>,
               public themeService: CustomizerSettingsService,
) {}

close(){
  this.dialogRef.close();
}
}
