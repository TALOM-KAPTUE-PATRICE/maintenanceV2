import { Component, Inject } from '@angular/core';
import { Ticket } from '../models/ticket.model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service';


@Component({
  selector: 'app-ticket-details',
  standalone: false,
  templateUrl: './ticket-details.component.html',
  styleUrl: './ticket-details.component.scss'
})
export class TicketDetailsComponent {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: Ticket,
    private dialogRef: MatDialogRef<TicketDetailsComponent>,
    public themeService: CustomizerSettingsService,
  ) { }

  close() {
    this.dialogRef.close();
  }

}
