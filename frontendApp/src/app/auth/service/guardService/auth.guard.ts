// src/app/auth/service/guardService/auth.guard.ts
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { map, take } from 'rxjs/operators';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.isAuthenticated$.pipe(
    take(1),
    map(isAuthenticated => {
      if (isAuthenticated) {
        return true; // OK, peut accéder à la route
      } else {
        // Pas connecté, rediriger vers login avec l'URL de retour
        console.warn('AuthGuard: User not authenticated, redirecting to login.');
        router.navigate(['/auth'], { queryParams: { returnUrl: state.url } });
        return false;
      }
    })
  );
};