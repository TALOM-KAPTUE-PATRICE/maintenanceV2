// src/app/home/profile/profile.component.ts
import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { AuthService } from '../../auth/service/auth.service'; // Ajustez le chemin
import { UserProfile } from '../../auth/models/auth.model';   // Ajustez le chemin
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common'; // Import pour *ngIf, *ngFor, etc.
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips'; // Pour afficher les permissions
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button'; // Pour un éventuel bouton d'édition
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CustomizerSettingsService } from '../dashboard/customizer-settings/customizer-settings.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-profile',
  standalone: true, // Rendre le composant standalone
  imports: [
    CommonModule,
    MatCardModule,
    MatListModule,
    MatIconModule,
    MatChipsModule,
    MatDividerModule,
    MatButtonModule,
    MatProgressSpinnerModule

  ],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'] // Changé en styleUrls
})
export class ProfileComponent implements OnInit, OnDestroy {
  // Utilisation de inject pour une syntaxe plus concise
  private authService = inject(AuthService);

  user: UserProfile | null = null;
  private userSubscription!: Subscription;

  // Base URL pour les images (si elles ne sont pas servies à la racine de l'application)
  // Si FileStorageService retourne "profile-images/nom.jpg" et que Spring Boot
  // sert ce dossier à la racine (/profile-images/nom.jpg), alors ceci est correct.
  // Sinon, si FileStorageService retourne un chemin complet ou un préfixe différent, ajustez.
  
  private apiUrl = `${environment.apiUrl.replace('/api', '')}`;

  constructor(
     public themeService: CustomizerSettingsService
  ) { }

  ngOnInit(): void {

    this.userSubscription = this.authService.currentUser$.subscribe(
      (userData) => {
        if (userData) {
          this.user = userData;
          console.log('Profil utilisateur chargé:', this.user);
          console.log('Permissions de l\'utilisateur:', this.user.permissions);
        } else {
          // Gérer le cas où l'utilisateur n'est pas (ou plus) connecté
          // Peut-être rediriger ou afficher un message.
          // Normalement, AuthGuard devrait empêcher d'arriver ici si non connecté.
          console.warn('Aucun utilisateur connecté trouvé dans ProfileComponent.');
        }
      }
    );
  }

  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }

  getUserImageUrl(imagePath?: string): string {
    if (imagePath) {
      if (imagePath.startsWith('http://') || imagePath.startsWith('https://')) {
        return imagePath;
      }
      const separator = this.apiUrl.endsWith('/') || imagePath.startsWith('/') ? '' : '/';
      return `${this.apiUrl}${separator}${imagePath}`;
    }
    return 'assets/images/logo.png';
  }

}