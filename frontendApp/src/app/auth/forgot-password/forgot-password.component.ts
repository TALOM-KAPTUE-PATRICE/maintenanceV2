import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../service/auth.service'; // Ajustez le chemin
import { MatSnackBar } from '@angular/material/snack-bar';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service'; // Ajustez le chemin
import { HttpErrorResponse } from '@angular/common/http'; // Pour typer l'erreur

@Component({
  selector: 'app-forgot-password',
  standalone: false, // Si vous le rendez standalone, ajoutez les imports ci-dessous
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss'] // Changé en styleUrls
})
export class ForgotPasswordComponent {
  forgotPasswordForm: FormGroup;
  isLoading = false; // Pour gérer l'état de chargement
  errorMessage: string | null = null; // Pour afficher les erreurs
  successMessage: string | null = null; // Pour afficher les messages de succès

  public themeService = inject(CustomizerSettingsService);
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private snackBar = inject(MatSnackBar);

  constructor() {
    this.forgotPasswordForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (this.forgotPasswordForm.invalid) {
      this.forgotPasswordForm.markAllAsTouched(); // Afficher les erreurs de validation
      return;
    }

    this.isLoading = true;
    const email = this.forgotPasswordForm.value.email;

    this.authService.forgotPassword(email).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.successMessage = response.message || 'Si un compte existe pour cet email, un lien de réinitialisation a été envoyé.';
        this.forgotPasswordForm.reset(); // Réinitialiser le formulaire après succès
        this.snackBar.open(this.successMessage, 'Fermer', {
          duration: 5000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['success-snackbar'] // Classe CSS pour le style
        });
      },
      error: (err: Error) => { // Typage de l'erreur
        this.isLoading = false;
        this.errorMessage = err.message || 'Une erreur est survenue lors de la demande de réinitialisation.';
        this.snackBar.open(this.errorMessage, 'Fermer', {
          duration: 5000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['error-snackbar'] // Classe CSS pour le style
        });
        console.error('Forgot password error:', err);
      }
    });
  }
}