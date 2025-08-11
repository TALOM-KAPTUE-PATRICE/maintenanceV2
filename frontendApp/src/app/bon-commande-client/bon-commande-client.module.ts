import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BonCommandeClientComponent } from './bon-commande-client/bon-commande-client.component';
import { BonCommandeClientListComponent } from './bon-commande-client-list/bon-commande-client-list.component';
import { RouterModule, Routes } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';

// Angular Material
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';

const routesBcClts: Routes =[
  {path : '' , component : BonCommandeClientComponent}
]

@NgModule({
  declarations: [
    BonCommandeClientComponent,
    BonCommandeClientListComponent

  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routesBcClts),
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatTableModule,
    MatPaginatorModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatTooltipModule
  ]
})
export class BonCommandeClientModule { }
