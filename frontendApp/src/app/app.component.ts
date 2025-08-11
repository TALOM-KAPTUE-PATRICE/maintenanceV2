import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CustomizerSettingsService } from './home/dashboard/customizer-settings/customizer-settings.service';

@Component({
  selector: 'app-root',
  standalone: false,
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'frontend';

  constructor(
    private router: Router,
    public themeService: CustomizerSettingsService,
  ){
    if (sessionStorage.getItem('token') || localStorage.getItem('token')) {
      this.router.navigate(['/home']); // Redirigez si déjà connecté
   }

  }
}
