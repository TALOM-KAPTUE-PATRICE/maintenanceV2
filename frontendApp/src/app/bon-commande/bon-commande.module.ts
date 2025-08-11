import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { BonCommandeComponent } from './bon-commande/bon-commande.component';
import { ListBonCommandeComponent } from './list-bon-commande/list-bon-commande.component';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatTableModule } from '@angular/material/table';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { BonCommandeDetailsComponent } from './bon-commande-details/bon-commande-details.component';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

const BonCommandesRoutes: Routes = [
  { path: '', component: BonCommandeComponent },
];

@NgModule({
  declarations: [
    BonCommandeComponent,
    ListBonCommandeComponent,
    BonCommandeDetailsComponent,
    

  ],
  imports: [
    CommonModule,
    RouterModule.forChild(BonCommandesRoutes),
    MatCardModule,
    MatButtonModule,
    MatMenuModule,
    MatTableModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    ReactiveFormsModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    MatPaginatorModule
  ],
})
export class BonCommandeModule {}
