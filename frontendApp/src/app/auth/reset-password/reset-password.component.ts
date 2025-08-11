import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms'; // Ajout AbstractControl, ValidationErrors
import { AuthService } from '../service/auth.service'; // Ajustez le chemin
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service'; // Ajustez le chemin
import { HttpErrorResponse } from '@angular/common/http'; // Pour typer l'erreur

// Validateur personnalisé pour vérifier que les mots de passe correspondent
export function passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
  const newPassword = control.get('newPassword');
  const confirmPassword = control.get('confirmPassword');

  if (newPassword && confirmPassword && newPassword.value !== confirmPassword.value) {
    confirmPassword.setErrors({ passwordMismatch: true });
    return { passwordMismatch: true };
  } else if (confirmPassword?.hasError('passwordMismatch')) {
    // Si les mots de passe correspondent maintenant, supprimer l'erreur (uniquement si elle a été définie par ce validateur)
     confirmPassword.setErrors(null);
  }
  return null;
}

@Component({
  selector: 'app-reset-password',
  standalone: false,
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss'] // Changé
})
export class ResetPasswordComponent implements OnInit {
  resetPasswordForm: FormGroup;
  hideNewPassword = true;
  hideConfirmPassword = true;
  token: string | null = null;
  isLoading = false;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  public themeService = inject(CustomizerSettingsService);
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  constructor() {
    this.resetPasswordForm = this.fb.group({
      // oldPassword: ['', Validators.required], // Généralement pas besoin de l'ancien mot de passe avec un token de reset
      newPassword: ['', [Validators.required, Validators.minLength(8)]], // Conserver minLength comme sur votre backend
      confirmPassword: ['', Validators.required]
    }, { validators: passwordMatchValidator }); // Appliquer le validateur personnalisé au groupe
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
      if (!this.token) {
        this.errorMessage = "Token de réinitialisation manquant ou invalide.";
        this.snackBar.open(this.errorMessage, 'Fermer', { duration: 5000, panelClass: ['error-snackbar'] });
        this.router.navigate(['/auth/forgot-password']); // Rediriger si pas de token
      }
    });
  }

  onSubmit(): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (this.resetPasswordForm.invalid) {
      this.resetPasswordForm.markAllAsTouched();
      return;
    }
    if (!this.token) {
        this.errorMessage = "Token de réinitialisation manquant. Veuillez refaire une demande.";
        this.snackBar.open(this.errorMessage, 'Fermer', { duration: 5000, panelClass: ['error-snackbar'] });
        return;
    }

    this.isLoading = true;
    const newPassword = this.resetPasswordForm.value.newPassword;

    this.authService.resetPassword(this.token, newPassword).subscribe({
      next: (response: any) => { // Typez la réponse si vous en attendez une spécifique
        this.isLoading = false;
        this.successMessage = response.message || "Mot de passe réinitialisé avec succès ! Vous pouvez maintenant vous connecter.";
        this.resetPasswordForm.reset();
        this.snackBar.open(this.successMessage, 'Fermer', {
          duration: 7000, // Plus de temps pour lire
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['success-snackbar']
        });
        this.router.navigate(['/auth']); // Rediriger vers la page de connexion
      },
      error: (err: Error) => {
        this.isLoading = false;
        this.errorMessage = err.message || "Erreur lors de la réinitialisation du mot de passe.";
        this.snackBar.open(this.errorMessage, 'Fermer', {
          duration: 5000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['error-snackbar']
        });
        console.error('Reset password error:', err);
      }
    });
  }
}