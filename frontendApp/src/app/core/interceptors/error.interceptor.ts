import { Injectable, inject } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../auth/service/auth.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  private snackBar = inject(MatSnackBar);
  private authService = inject(AuthService);
  private isRefreshing = false; // Pour gérer les futures stratégies de rafraîchissement de token


  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        
        let errorMessage = 'Une erreur inattendue est survenue.';
        let panelClass = 'error-snackbar'; // Classe CSS par défaut pour les erreurs

     // Gérer le cas 401 : Token invalide ou expiré
        if (error.status === 401) {
          // On vérifie que l'erreur ne vient pas de la page de login elle-même
          if (!request.url.includes('/auth')) {
            errorMessage = 'Votre session a expiré. Veuillez vous reconnecter.';
            // APPEL CLÉ : On délègue la déconnexion au service d'authentification
            this.authService.logout();
          } else {
             // Si l'erreur 401 vient de la tentative de login, le message est différent
             errorMessage = "L'email ou le mot de passe est incorrect.";
          }
        } else if (error.status === 403) {
          errorMessage = "Accès refusé. Vous n'avez pas les permissions nécessaires.";
        } else if (error.status === 404) {
          errorMessage = 'La ressource demandée n\'a pas été trouvée.';
        } else if (error.status === 500) {
          errorMessage = 'Erreur interne du serveur. Veuillez contacter le support.';
        } else if (error.error && error.error.message) {
          // Gérer les erreurs métier renvoyées par Spring (ex: DuplicateResourceException)
          errorMessage = error.error.message;
        }

        // Afficher le message d'erreur dans un snackbar
        this.snackBar.open(errorMessage, 'Fermer', {
          duration: 9000,
          panelClass: [panelClass],
          horizontalPosition: 'center',
          verticalPosition: 'top',
        });

        // Renvoyer l'erreur pour que le code appelant puisse aussi la gérer si besoin
        return throwError(() => error);
      })
    );
  }
}