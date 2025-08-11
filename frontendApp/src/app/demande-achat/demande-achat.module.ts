import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ListeDaComponent } from './liste-da/liste-da.component';
import { DemandeAchatComponent } from './demande-achat/demande-achat.component';
import { RouterModule, Routes } from '@angular/router';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { DemandeAchatService } from './daServices/demande-achat.service';
import { ReactiveFormsModule } from '@angular/forms';
import { DemandeAchatDetailsDialogComponent } from './demande-achat-details-dialog/demande-achat-details-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

const routesDa : Routes = [
  { path : '',
    component: DemandeAchatComponent
  }

]



@NgModule({
  declarations: [
    ListeDaComponent,
    DemandeAchatComponent,
    DemandeAchatDetailsDialogComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routesDa),
    MatCardModule,
    MatButtonModule,
    MatMenuModule,
    MatTableModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatDatepickerModule,
    MatNativeDateModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatPaginatorModule
    
  ],
  providers: [
    DemandeAchatService
  ]
})
export class DemandeAchatModule { }
