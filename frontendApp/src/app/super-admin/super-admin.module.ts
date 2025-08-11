import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { SuperAdminComponent } from './super-admin/super-admin.component';
import { FooterComponent } from './super-admin/footer/footer.component';
import { HeaderComponent } from './super-admin/header/header.component';
import { SidebarComponent } from './super-admin/sidebar/sidebar.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { NgScrollbarModule } from 'ngx-scrollbar';
import { CustomizerSettingsComponent } from './super-admin/customizer-settings/customizer-settings.component';


const routeSuperAdmin: Routes = [
  { path: '', component: SuperAdminComponent }
]

@NgModule({
  declarations: [
    SuperAdminComponent,
    FooterComponent,
    HeaderComponent,
    SidebarComponent,
    CustomizerSettingsComponent


  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routeSuperAdmin),
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
    MatDividerModule,
    MatIconModule,
    MatExpansionModule,
    NgScrollbarModule,
  ]
})
export class SuperAdminModule { }
