import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

// Components
import { LandingPageComponent } from './landing-page/landing-page.component';

// Angular Material & Autres
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatTabsModule } from '@angular/material/tabs';
import { CarouselModule } from 'ngx-owl-carousel-o';
import { MatDividerModule } from '@angular/material/divider';

const routes: Routes = [
   { path: '', component: LandingPageComponent }
];

@NgModule({
  declarations: [
    LandingPageComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatExpansionModule,
    MatTabsModule,
    CarouselModule,
    MatDividerModule
  ],
  exports: [
    RouterModule
  ]
})
export class LandingModule { }