import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { TravauxComponent } from './travaux/travaux.component';
import { MatListModule, MatNavList } from '@angular/material/list';
import { PlanningComponent } from './planning/planning.component';
import { PrestataireComponent } from './prestataire/prestataire.component';
import { ReceptionTravauxComponent } from './reception-travaux/reception-travaux.component';
import { BudjetComponent } from './budjet/budjet.component';
import { MatTableModule } from '@angular/material/table';
import { DxGanttModule } from 'devextreme-angular';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import { MatCard, MatCardContent, MatCardHeader, MatCardModule, MatCardTitle } from '@angular/material/card';
import { MatProgressBarModule } from '@angular/material/progress-bar';


const TravauxRoutes: Routes = [
  { path: '', 
    component: TravauxComponent, 
    children: [       
      { path: '', redirectTo: 'liste', pathMatch: 'full'},
      { path : 'liste' , component: PlanningComponent},      
      { path : 'planning' ,  component: PlanningComponent},
      { path : 'prestataire', component: PrestataireComponent},
      { path : 'reception', component: ReceptionTravauxComponent},
      { path: 'budjet', component: BudjetComponent}
      
    ]
  },
]

@NgModule({
  declarations: [
    TravauxComponent,    
  ],
  imports: [  
    CommonModule,
    RouterModule.forChild(TravauxRoutes),
    MatListModule,
    MatNavList,
    MatTableModule,
    DxGanttModule,
    MatIconModule,
    MatPaginatorModule,
    MatCardModule,
    MatCardContent,
    MatCardHeader,
    MatCardTitle,
    MatCard,
    MatProgressBarModule,     
    
  ]
})
export class SuiviTravauxModule { }
