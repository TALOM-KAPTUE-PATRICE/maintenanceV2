import { Component,inject,Inject } from '@angular/core';

import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Devis } from '../model/devis.model';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service';

@Component({
  selector: 'app-devis-details',
  standalone: false,
  templateUrl: './devis-details.component.html',
  styleUrl: './devis-details.component.scss'
})
export class DevisDetailsComponent {

  public themeService = inject(CustomizerSettingsService);

  constructor(@Inject(MAT_DIALOG_DATA) public data: Devis,
              private dialogRef: MatDialogRef<DevisDetailsComponent>
) {}

    close(){
      this.dialogRef.close();
    }
}
