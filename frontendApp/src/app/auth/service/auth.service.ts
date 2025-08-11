// src/app/auth/service/auth.service.ts
import { Injectable, NgZone, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject, throwError, of } from 'rxjs';
import { catchError, tap, map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AuthResponse, UserProfile } from '../models/auth.model'; // Ajustez le chemin
import { jwtDecode } from "jwt-decode"; // Installation : npm install jwt-decode
import { environment } from '../../../../src/environments/environment'; // Utiliser l'environnement



interface DecodedToken {
  sub: string; // Subject (email)
  role: string;
  poste: string;
  permissions: string[];
  iat: number; // Issued at
  exp: number; // Expiration time
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`; 
  private router = inject(Router);
  private http = inject(HttpClient);
  private ngZone = inject(NgZone); // Injecter NgZone pour les redirections fiables

  // BehaviorSubjects pour l'état de l'utilisateur et les permissions
  private currentUserSubject = new BehaviorSubject<UserProfile | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  private storage: Storage = sessionStorage;

  constructor() {
         // Cette méthode va vérifier le stockage au démarrage et mettre à jour les BehaviorSubjects
    this.loadUserFromStorage();
  }


    // NOUVELLE MÉTHODE : Centralise la vérification au démarrage
  private loadUserFromStorage(): void {
    const token = this.getToken();
    if (token && !this.isTokenExpired(token)) {
      const user = this.getStoredUser();
      if (user) {
        this.currentUserSubject.next(user);
        this.isAuthenticatedSubject.next(true);
        console.log('AuthService: Session restaurée depuis le stockage.');
      } else {
        // Token présent mais pas de données utilisateur -> incohérence, on nettoie
        this.logout();
      }
    } else {
      // Pas de token valide, on s'assure que l'état est propre
      this.clearStorage();
    }
  }


  public get currentUserValue(): UserProfile | null {
    return this.currentUserSubject.value;
  }

  login(credentials: { email: string; password: string }, rememberMe: boolean): Observable<AuthResponse> {
    this.storage = rememberMe ? localStorage : sessionStorage;

    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        // La méthode storeUserSession s'occupe de mettre à jour les BehaviorSubjects
        this.storeUserSession(response);
      }),
      // La gestion d'erreur est déléguée à handleError
      catchError((error) => this.handleError(error))
    );
  }

  private storeUserSession(response: AuthResponse): void {
    if (!response || !response.token || !response.user) {
      console.error('Tentative de stocker une session invalide.');
      this.logout(); // Nettoyer en cas de réponse invalide
      return;
    }
    this.storage.setItem('token', response.token);
    this.storage.setItem('currentUser', JSON.stringify(response.user));
    this.currentUserSubject.next(response.user);
    this.isAuthenticatedSubject.next(true); // C'est ce qui va déclencher le guard !
  }

  private getStoredUser(): UserProfile | null {
    const userStr = localStorage.getItem('currentUser') || sessionStorage.getItem('currentUser');
    if (userStr) {
      try {
        return JSON.parse(userStr) as UserProfile;
      } catch (e) {
        console.error("Erreur de parsing de l'utilisateur stocké", e);
        this.clearStorage(); // Nettoyer si corrompu
        return null;
      }
    }
    return null;
  }

  private clearStorage(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('currentUser');
  }
  
  private clearUserAndRedirectToLogin(redirect: boolean = true): void {
    this.clearStorage();
    this.currentUserSubject.next(null);
    this.isAuthenticatedSubject.next(false);
    if (redirect) {
      this.router.navigate(['/auth']); // Ajustez le chemin de votre page de login
    }
  }

  logout(): void {
    const token = this.getToken();
    const logoutCall$ = token ? this.http.post(`${this.apiUrl}/logout`, {}) : of(null); // Crée un Observable qui fait rien si pas de token

    logoutCall$.subscribe({
      next: () => {
        console.log('Token invalidé côté serveur (ou pas de token à invalider).');
        // Le nettoyage se fait après la confirmation (ou immédiatement si pas de token)
        this.performLocalLogout();
      },
      error: (err) => {
        console.error("Erreur lors de l'invalidation du token, déconnexion locale quand même.", err);
        // TRÈS IMPORTANT : On déconnecte l'utilisateur localement MÊME SI l'appel API échoue.
        // L'utilisateur ne doit pas rester bloqué dans un état "connecté" si le serveur ne répond pas.
        this.performLocalLogout();
      }
    });
  }

    // NOUVELLE MÉTHODE PRIVÉE pour la logique de nettoyage
  private performLocalLogout(): void {
    this.clearStorage();
    this.currentUserSubject.next(null);
    this.isAuthenticatedSubject.next(false);
    
    this.ngZone.run(() => {
      this.router.navigate(['/auth']);
    });
    console.log('User logged out locally.');
  }
  

  
  isLoggedIn(): boolean {
    const token = this.getToken();
    if (token && !this.isTokenExpired(token)) {
      // Si un utilisateur est dans le BehaviorSubject, on peut le considérer comme loggué
      // Sinon, on pourrait vouloir vérifier à nouveau avec le backend si le token est toujours valide
      // Pour l'instant, la présence d'un token non expiré est suffisante.
      return true;
    }
    return false;
  }

  getToken(): string | null {
    return localStorage.getItem('token') || sessionStorage.getItem('token');
  }

  private getDecodedToken(): DecodedToken | null {
    const token = this.getToken();
    if (token) {
      try {
        return jwtDecode<DecodedToken>(token);
      } catch (Error) {
        console.error("Erreur de décodage du token:", Error);
        this.clearUserAndRedirectToLogin(); // Si le token est invalide, déconnecter
        return null;
      }
    }
    return null;
  }

  isTokenExpired(tokenToCheck?: string): boolean {
    const token = tokenToCheck || this.getToken();
    if (!token) return true;

    try {
      const decoded = jwtDecode<DecodedToken>(token);
      const expirationDate = decoded.exp * 1000;
      const isExpired = expirationDate < Date.now();
      if (isExpired) {
          console.warn('Token is expired.');
      }
      return isExpired;
    } catch (error) {
      console.error('Invalid token for expiration check:', error);
      return true; // Considérer comme expiré si invalide
    }
  }

  hasPermission(requiredPermission: string | string[]): boolean {
    const user = this.currentUserValue;
    if (!user || !user.permissions) {
      return false;
    }

    if (Array.isArray(requiredPermission)) {
      // Vérifie si l'utilisateur a AU MOINS UNE des permissions requises
      return requiredPermission.some(p => user.permissions.includes(p));
    } else {
      return user.permissions.includes(requiredPermission);
    }
  }

  // Vérifie si l'utilisateur a TOUTES les permissions requises
  hasAllPermissions(requiredPermissions: string[]): boolean {
    const user = this.currentUserValue;
    if (!user || !user.permissions) {
      return false;
    }
    return requiredPermissions.every(p => user.permissions.includes(p));
  }

  hasPoste(requiredPoste: string | string[]): boolean {
    const user = this.currentUserValue;
    if (!user || !user.poste) {
      return false;
    }
    if (Array.isArray(requiredPoste)) {
      return requiredPoste.some(p => user.poste.toUpperCase() === p.toUpperCase());
    } else {
      return user.poste.toUpperCase() === requiredPoste.toUpperCase();
    }
  }

  hasRole(requiredRole: string | string[]): boolean {
    const user = this.currentUserValue;
    if (!user || !user.role) {
      return false;
    }
    const userRoleUpper = user.role.toUpperCase();
    if (Array.isArray(requiredRole)) {
      return requiredRole.some(r => userRoleUpper === r.toUpperCase());
    } else {
      return userRoleUpper === requiredRole.toUpperCase();
    }
  }

  private hasToken(): boolean {
      const token = this.getToken();
      return !!token && !this.isTokenExpired(token);
  }

  // --- Méthodes pour mot de passe oublié ---
  forgotPassword(email: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/forgot-password`, { email })
      .pipe(catchError(this.handleError));
  }

  resetPassword(token: string, newPassword: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/reset-password`, { token, newPassword })
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Une erreur inconnue est survenue !';
    if (error.status === 401) {
      errorMessage = 'Email ou mot de passe incorrect.';
    } else if (error.error && error.error.message) {
      errorMessage = error.error.message;
    } else if (error.message) {
      errorMessage = `Erreur serveur: ${error.status}`;
    }
    console.error(`Erreur d'authentification: ${errorMessage}`, error);
    return throwError(() => new Error(errorMessage));
  }

}