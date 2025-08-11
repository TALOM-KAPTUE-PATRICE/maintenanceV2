import { NgModule } from '@angular/core';
import { CommonModule, NgClass } from '@angular/common';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ProfileComponent } from './profile/profile.component';
import { RouterLink, RouterModule, Routes, RouterLinkActive } from '@angular/router';
import { authGuard } from '../auth/service/guardService/auth.guard';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatMenuModule } from '@angular/material/menu';
import { MatCardModule } from '@angular/material/card';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule} from '@angular/material/list';
import { DashboardContentComponent } from './dashboard/dashboard-content/dashboard-content.component';
import { MatExpansionModule, MatExpansionPanel } from '@angular/material/expansion';
import { MatDividerModule } from '@angular/material/divider';
import { SidebarComponent } from './dashboard/sidebar/sidebar.component';
import { HeaderComponent } from './dashboard/header/header.component';
import { FooterComponent } from './dashboard/footer/footer.component';
import { CustomizerSettingsComponent } from './dashboard/customizer-settings/customizer-settings.component';
import { NgScrollbarModule } from 'ngx-scrollbar';
import { CarouselModule } from 'ngx-owl-carousel-o';
import { MatNativeDateModule, MatOptionModule } from '@angular/material/core';
import { IconsModule } from '../icons/icons.module';
import { AllProjectsComponent } from './dashboard/project-management/all-projects/all-projects.component';
import { MyTasksComponent } from './dashboard/project-management/my-tasks/my-tasks.component';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatPaginatorModule } from '@angular/material/paginator';
import { ProjectAnalysisComponent } from './dashboard/project-management/project-analysis/project-analysis.component';
import { ProjectsOverviewComponent } from './dashboard/project-management/projects-overview/projects-overview.component';
import { ProjectsProgressComponent } from './dashboard/project-management/projects-progress/projects-progress.component';
import { ProjectsRoadmapComponent } from './dashboard/project-management/projects-roadmap/projects-roadmap.component';
import { TasksOverviewComponent } from './dashboard/project-management/tasks-overview/tasks-overview.component';
import { TeamMembersComponent } from './dashboard/project-management/team-members/team-members.component';
import { ToDoListComponent } from './dashboard/project-management/to-do-list/to-do-list.component';
import { WorkingScheduleComponent } from './dashboard/project-management/working-schedule/working-schedule.component';
import { ProjectManagementComponent } from './dashboard/project-management/project-management.component';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule, MatLabel, MatPrefix } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatCalendar, MatCalendarBody,MatDatepickerModule } from '@angular/material/datepicker';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { RegleConfidentialiteComponent } from './regle-confidentialite/regle-confidentialite.component';
import { ConditionUtilisationComponent } from './condition-utilisation/condition-utilisation.component';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { ChartComponent, NgApexchartsModule } from 'ng-apexcharts'; // Importer NgApexchartsModule
import { HasPermissionDirective } from '../auth/directives/has-permission.directive';

const Homeroutes: Routes = [
    { path: '',
      component: DashboardComponent,

      children: [       
        { path: '', redirectTo: 'dashboard', pathMatch: 'full'},
        { path: 'profile', component: ProfileComponent, canActivate: [authGuard]},
        { path: 'dashboard', component: ProjectManagementComponent},
        { path: 'reglesconfidentielles', component : RegleConfidentialiteComponent},
        { path: 'conditionutilisation', component: ConditionUtilisationComponent},
        { path: 'bonCommande', loadChildren : () => import('../bon-commande/bon-commande.module').then(m => m.BonCommandeModule) },
        { path: 'tickets', loadChildren : () => import('../ticket/ticket.module').then(m => m.TicketModule) },
        { path: 'da', loadChildren : () => import('../demande-achat/demande-achat.module').then(m => m.DemandeAchatModule) },
        { path: 'devis', loadChildren : () => import('../devis/devis.module').then(m => m.DevisModule) },
        { path: 'facture', loadChildren : () => import('../facture/facture.module').then(m => m.FactureModule) },
        { path: 'travaux', loadChildren : () => import('../suivi-travaux/suivi-travaux.module').then(m => m.SuiviTravauxModule) },
        { path: 'bc-clients', loadChildren : () => import('../bon-commande-client/bon-commande-client.module').then(m => m.BonCommandeClientModule) },
        { path: 'users', loadChildren : () => import('../user/user.module').then(m => m.UserModule) },
                       
      ]
    }
]

@NgModule({
  declarations: [
    DashboardComponent,
    DashboardContentComponent,
    SidebarComponent,
    HeaderComponent,
    FooterComponent,
    CustomizerSettingsComponent,
    AllProjectsComponent,
    MyTasksComponent,
    ProjectAnalysisComponent,
    ProjectsOverviewComponent,
    ProjectsProgressComponent,
    ProjectsRoadmapComponent,
    TasksOverviewComponent,
    TeamMembersComponent,
    ToDoListComponent,
    WorkingScheduleComponent,
    ProjectManagementComponent

  ],
  imports: [
    CommonModule,
    RouterModule.forChild(Homeroutes),   
    IconsModule,
    MatButtonModule,
    MatCardModule,
    MatGridListModule,
    MatIconModule,
    MatMenuModule,
    MatListModule,
    MatSidenavModule,
    MatToolbarModule,
    RouterLink,
    NgApexchartsModule,
    ChartComponent,
    MatExpansionModule,
    RouterLinkActive, 
    NgClass,
    MatDividerModule,
    NgScrollbarModule,
    CarouselModule,
    MatNativeDateModule,

    MatCheckboxModule,
    MatExpansionPanel,
    RouterLinkActive,
    MatIcon,
    MatOptionModule,
    MatInputModule,
    MatFormFieldModule,
    MatLabel,
    MatSelectModule,
    MatDatepickerModule,
    MatPrefix,
    MatProgressSpinner,
    MatCalendar,
    MatCalendarBody,
    MatTableModule,
    MatPaginatorModule,
    MatProgressBarModule,
    HasPermissionDirective

  ],
  
  exports:[
    RouterModule,
    MatButtonModule,
    MatCardModule,
    MatGridListModule,
    MatIconModule,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatSidenavModule,
    MatToolbarModule,
    RouterLink,
  ]
})

export class HomeModule { }
