import { Component, OnInit } from '@angular/core';
import { ToggleService } from './header/toggle.service';
import { CustomizerSettingsService } from './customizer-settings/customizer-settings.service';

@Component({
  selector: 'app-super-admin',
  standalone: false,
  templateUrl: './super-admin.component.html',
  styleUrl: './super-admin.component.scss'
})
export class SuperAdminComponent implements OnInit {

   // isToggled
        isToggled = false;
  
        // isSidebarToggled
        isSidebarToggled = false;
  
        constructor(
            private toggleService: ToggleService,
            public themeService: CustomizerSettingsService,
  
        ) {
            this.toggleService.isSidebarToggled$.subscribe(isSidebarToggled => {
                this.isSidebarToggled = isSidebarToggled;
            });
            this.themeService.isToggled$.subscribe(isToggled => {
                this.isToggled = isToggled;
            });
        }
  
        ngOnInit(): void {
          // const token = this.authService.getToken();
          // if (token && !this.authService.isTokenExpired(token)) {
          //   this.authService.logout();
          //   this.router.navigate(['/auth']); // Rediriger vers la page de connexion
          // }
  
        }

}
