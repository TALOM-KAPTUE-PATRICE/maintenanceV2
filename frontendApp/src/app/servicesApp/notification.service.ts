import { Injectable } from '@angular/core';
// import { RxStomp } from '@stomp/rx-stomp';
// import SockJS from 'sockjs-client';
// import { environment } from '../../environments/environment';
// import { AuthService } from '../auth/service/auth.service';
// import { filter, switchMap } from 'rxjs/operators';
// import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  // private rxStomp: RxStomp;
  // public notifications$: Observable<any>; // Stream des notifications reçues

  // constructor(private authService: AuthService) {
  //   this.rxStomp = new RxStomp();

  //   // Configuration de la connexion STOMP
  //   this.rxStomp.configure({
  //     // Utilisation de SockJS pour la compatibilité
  //     webSocketFactory: () => {
  //       return new SockJS(`${environment.apiUrl.replace('/api', '')}/ws`);
  //     },
  //     // Headers de connexion (on peut y mettre le token JWT)
  //     connectHeaders: {},
  //     // Configuration pour la reconnexion, le debug, etc.
  //     reconnectDelay: 5000,
  //     debug: (msg: string): void => {
  //       console.log(new Date(), msg);
  //     }
  //   });

  //   // On écoute les notifications seulement si l'utilisateur est authentifié
  //   this.notifications$ = this.authService.isAuthenticated$.pipe(
  //     filter(isAuthenticated => isAuthenticated), // Ne continue que si l'utilisateur est connecté
  //     switchMap(() => {
  //       console.log('Utilisateur authentifié, abonnement au topic de notifications...');
  //       // L'utilisateur doit s'abonner à sa file d'attente personnelle
  //       return this.rxStomp.watch('/user/queue/notifications');
  //     })
      
  //   );
    
  //   // Activer la connexion STOMP
  //   this.rxStomp.activate();
  // }

  // // Cette méthode n'est plus nécessaire car le service s'auto-configure.
  // // On peut la garder pour un usage futur si besoin de déconnecter/reconnecter manuellement.
  // public disconnect(): void {
  //   this.rxStomp.deactivate();
  // }

}