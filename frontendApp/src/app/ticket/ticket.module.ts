import { NgModule, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { TicketComponent } from './ticket/ticket.component';
import { TicketService } from './TicketService/ticket.service';
import { TicketDialogComponent } from './ticket-dialog/ticket-dialog.component';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatMenuModule } from '@angular/material/menu';
import { MatTableModule } from '@angular/material/table';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { DtToDoListComponent } from './dt-to-do-list/dt-to-do-list.component';
import { ReactiveFormsModule } from '@angular/forms';
import { TicketDetailsComponent } from './ticket-details/ticket-details.component';
import {  MatDialogModule } from '@angular/material/dialog';
import { MatPaginatorModule } from '@angular/material/paginator';
import {  MatProgressSpinnerModule } from '@angular/material/progress-spinner';


const TicketRoutes: Routes =[
  { path: '' ,
    component : TicketComponent
  }

]


@NgModule({
  declarations: [
    TicketComponent,
    TicketDialogComponent,
    DtToDoListComponent,
    TicketDetailsComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(TicketRoutes),
    MatIconModule,
    MatFormFieldModule,
    FormsModule,
    MatLabel,
    MatCardModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatMenuModule,
    MatTableModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatDialogModule,
    MatPaginatorModule,  
    MatProgressSpinnerModule
    
  ],
  providers:[
    TicketService
  ]
})
export class TicketModule { }
