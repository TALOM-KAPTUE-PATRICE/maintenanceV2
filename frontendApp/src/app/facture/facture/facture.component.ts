import { Component } from '@angular/core';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service';


@Component({
  selector: 'app-facture',
  standalone: false,
  templateUrl: './facture.component.html',
  styleUrl: './facture.component.scss'
})
export class FactureComponent {

  constructor(
    public themeService: CustomizerSettingsService,
  ){}

}
