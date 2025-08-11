import { Component } from '@angular/core';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service';

@Component({
  selector: 'app-demande-achat',
  standalone: false,
  templateUrl: './demande-achat.component.html',
  styleUrl: './demande-achat.component.scss'
})
export class DemandeAchatComponent {

  constructor(public themeService: CustomizerSettingsService) {}

  ngOnInit(): void {
    
  }
}
