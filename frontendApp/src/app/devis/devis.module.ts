import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DevisComponent } from './devis/devis.component';
import { ListeDevisComponent } from './liste-devis/liste-devis.component';
import { RouterModule, Routes } from '@angular/router';
import { DevisService } from './devisService/devis.service';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatMenuModule } from '@angular/material/menu';
import { MatTableModule } from '@angular/material/table';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { DevisDetailsComponent } from './devis-details/devis-details.component';
import { ArticleComponent } from './article/article.component';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionPanel, MatExpansionPanelHeader, MatExpansionPanelTitle } from '@angular/material/expansion';

const DevisRoutes : Routes = [
   { path: '' , component : DevisComponent}
  
]

@NgModule({
  declarations: [    
    DevisComponent,
    ListeDevisComponent,
    DevisDetailsComponent,
    ArticleComponent
  ],
  imports: [

    CommonModule,
    RouterModule.forChild(DevisRoutes),
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
    MatDialogModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatIconModule,
    MatDividerModule,
    MatExpansionPanel,
    MatExpansionPanelHeader,  
    MatExpansionPanelTitle

  ],
  providers: [
    DevisService
  ]
})
export class DevisModule { }
