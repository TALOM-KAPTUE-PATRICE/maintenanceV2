import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { SuccessDialogComponentComponent, SuccessDialogData } from '../../success-dialog-component/success-dialog-component.component';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service';
import { first } from 'rxjs/operators'; // Importer 'first'


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  standalone: false,
})

export class LoginComponent {

  authForm: FormGroup;
  hide = true;
  rememberMe = false;
  loginError: string | null = null; // Pour afficher les erreurs de login
  isLoading = false; // Pour gérer l'état de chargement

  // Utilisation de inject pour une syntaxe plus concise (facultatif)
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private authService = inject(AuthService);
  private dialog = inject(MatDialog); // Renommé pour clarté
  public themeService = inject(CustomizerSettingsService);

  constructor() {
    this.authForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]], // Peut-être min 6 ou 8
    });

  }

  onSubmit() {
    this.loginError = null; // Réinitialiser l'erreur
    if (this.authForm.invalid) {
      // Marquer tous les champs comme 'touched' pour afficher les erreurs de validation
      this.authForm.markAllAsTouched();
      console.log('Formulaire invalide. Veuillez vérifier les champs.');
      return;
    }

    this.isLoading = true;
    const { email, password } = this.authForm.value;

     this.authService.login({ email, password }, this.rememberMe)
      .pipe(first()) // Prend la première réponse et se désabonne automatiquement
      .subscribe({
        next: (response) => {
          this.isLoading = false;
          // La redirection est maintenant gérée par le guard
          // On peut toujours afficher un message de succès
          this.openSuccessDialog(`Connexion réussie, Bienvenue ${response.user.nom} !`);

        },
        error: (err: Error) => {
          this.isLoading = false;
          this.loginError = err.message;
        }
      });
  }

  openSuccessDialog(message: string): void {
    const dialogData: SuccessDialogData = { // Utiliser l'interface
      title: 'Connexion Réussie',
      message: message,
      // autoCloseDelay: 3000 // Optionnel: fermer après 3 secondes
    };

    const dialogRef = this.dialog.open(SuccessDialogComponentComponent, {
      width: '420px', // Un peu plus large pour le nouveau design
      data: dialogData,
      disableClose: true // Empêche la fermeture en cliquant à l'extérieur ou avec Echap (si autoClose est actif)
    });

    // Si autoCloseDelay n'est pas utilisé, l'utilisateur doit cliquer sur OK.
    // Si autoCloseDelay est utilisé, le disableClose peut être utile pour forcer l'attente.
  }

}

// responseIdentification(response: any) {

//   const responsePayload = this.decodeJwtResponse(response.credential);

//   console.log('Response Payload:', responsePayload);
//   // Obtenez les informations nécessaires
//   const userEmail = responsePayload.email;
//   const userName = responsePayload.name;
//   const userImage = responsePayload.picture;

//   // Préparez les données pour l'API
//   const userData = {
//       email: userEmail,
//       nom: userName,
//       imagePath: userImage
//   };

//   // Appelez votre API pour vérifier l'utilisateur
//   this.authService.loginWithGoogle(userData).subscribe(
//       (res: any) => {
//           // Gérer la réponse, par exemple, stocker le token
//           localStorage.setItem('token', res.token);
//           this.router.navigate(['/home']); // Redirigez vers le tableau de bord
//       },
//       (error) => {
//           console.error('Error during Google login:', error);
//       }
//   );
// }

// decodeJwtResponse(token: string) {

//   const payload = token.split('.')[1];
//   console.log(JSON.parse(atob(payload)));
//   return JSON.parse(atob(payload));
//    // Décodez le payload
// }


