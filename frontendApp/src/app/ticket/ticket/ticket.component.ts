import { Component } from '@angular/core';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service';


@Component({
  selector: 'app-ticket',
  standalone: false,
  templateUrl: './ticket.component.html',
  styleUrl: './ticket.component.scss'
})
export class TicketComponent {

  constructor(public themeService: CustomizerSettingsService) {}

  ngOnInit(): void {
    
  }
  

}
