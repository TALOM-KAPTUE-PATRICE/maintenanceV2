import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { map, take } from 'rxjs/operators';

export const loginGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.isAuthenticated$.pipe(
    take(1),
    map(isAuthenticated => {
      if (isAuthenticated) {
        // Si l'utilisateur est déjà connecté, on le redirige vers /home
        console.log('LoginGuard: User already authenticated, redirecting to home.');
        router.navigate(['/home']);
        return false; // Empêche l'accès à la page de login
      } else {
        // Pas connecté, peut accéder à la page de login
        return true;
      }
    })
  );
};