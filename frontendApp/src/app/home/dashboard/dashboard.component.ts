import { Component, OnInit} from '@angular/core';
import { ToggleService } from './header/toggle.service';
import { CustomizerSettingsService } from './customizer-settings/customizer-settings.service';



@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})


export class DashboardComponent implements OnInit  {
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


      }

}
