import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service';


@Component({
  selector: 'app-facture-details',
  standalone: false,
  templateUrl: './facture-details.component.html',
  styleUrl: './facture-details.component.scss'
})
export class FactureDetailsComponent {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any , private dialogRef: MatDialogRef<FactureDetailsComponent>, public themeService: CustomizerSettingsService,) {}

  close(){
     this.dialogRef.close();
   }
}
