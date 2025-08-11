import { Injectable, inject } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth/service/auth.service';
import { environment } from '../../environments/environment';


@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    private authService = inject(AuthService);

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = this.authService.getToken();
        const apiUrl = environment.apiUrl; // ex: http://localhost:8081/api

        // ▼▼▼ LOGIQUE AMÉLIORÉE ▼▼▼

        // Les requêtes pour l'API doivent avoir le token (sauf pour /auth)
        if (req.url.startsWith(apiUrl) && !req.url.includes('/auth/')) {
            if (token) {
                const cloned = req.clone({
                    headers: req.headers.set('Authorization', `Bearer ${token}`)
                });
                return next.handle(cloned);
            }
            // Si pas de token pour une route API protégée, le backend renverra une 401
            // qui sera interceptée par ErrorInterceptor.
            return next.handle(req);
        }

        // Pour toutes les autres requêtes (ressources locales, images du serveur, etc.),
        // ne rien faire et passer la requête telle quelle.
        // Cela couvre automatiquement les appels à /profile-images, /b_commandes_client, etc.
        // car ils ne commencent pas par /api.
        return next.handle(req);
    }
}