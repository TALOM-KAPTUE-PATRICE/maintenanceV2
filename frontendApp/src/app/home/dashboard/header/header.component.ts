import { Component, HostListener, inject, Inject, OnDestroy, OnInit, PLATFORM_ID } from '@angular/core';
import { RouterLink, NavigationEnd, Router } from '@angular/router';
import { ToggleService } from './toggle.service';
import { NgClass, isPlatformBrowser } from '@angular/common';
import { CustomizerSettingsService } from '../customizer-settings/customizer-settings.service';
import { filter, takeUntil } from 'rxjs/operators';
import { AuthService } from '../../../auth/service/auth.service';
import { NotificationService } from '../../../servicesApp/notification.service';

import { UserProfile } from '../../../auth/models/auth.model';
import { Observable, Subject } from 'rxjs';
import { environment } from '../../../../environments/environment';


@Component({
    selector: 'app-header',
    standalone: false,
    templateUrl: './header.component.html',
    styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit, OnDestroy {

    // Utilisation de inject pour une syntaxe plus moderne
    private toggleService = inject(ToggleService);
    public themeService = inject(CustomizerSettingsService);
    @Inject(PLATFORM_ID) private platformId = inject(PLATFORM_ID);
    private router = inject(Router);
    private authService = inject(AuthService);
    private notificationService = inject(NotificationService);

    // Rendre currentUser public pour l'utiliser avec le pipe async dans le template
    public currentUser$: Observable<UserProfile | null>;
    public user: UserProfile | null = null; // Peut toujours être utile pour un accès direct dans le TS

    messages: Notification[] = [];
    isSidebarToggled = false;
    isToggled = false; // Pour les settings du themeService
    isSticky: boolean = false;
    isFullscreen: boolean = false;

    private destroy$ = new Subject<void>(); // Pour se désabonner proprement

    // Base URL pour les images (identique à ProfileComponent)
    private imageBaseUrl = 'http://localhost:8081'; // Adaptez le port si nécessaire
    snackBar: any;

    private apiUrl = `${environment.apiUrl.replace('/api', '')}`;

    constructor() {
        this.currentUser$ = this.authService.currentUser$;

        this.toggleService.isSidebarToggled$.pipe(
            takeUntil(this.destroy$)
        ).subscribe(isSidebarToggled => {
            this.isSidebarToggled = isSidebarToggled;
        });

        this.router.events.pipe(
            filter(event => event instanceof NavigationEnd),
            takeUntil(this.destroy$)
        ).subscribe(() => {
            if (this.isSidebarToggled && isPlatformBrowser(this.platformId)) { // Vérifier la plateforme pour les manips de DOM/UI
                // Fermer la sidebar si elle est ouverte lors d'un changement de route (surtout sur mobile)
                // this.toggleService.toggle(); // Décommentez si c'est le comportement désiré
            }
        });

        this.themeService.isToggled$.pipe(
            takeUntil(this.destroy$)
        ).subscribe(isToggled => {
            this.isToggled = isToggled;
        });
    }

    ngOnInit(): void {
        // S'abonner à currentUser$ pour mettre à jour la propriété 'user' locale si besoin
        this.authService.currentUser$.pipe(
            takeUntil(this.destroy$)
        ).subscribe(userData => {
            this.user = userData;
            if (this.user) {
                console.log('Header: User data loaded:', this.user);
            } else {
                console.log('Header: No user data.');

            }
        });

        // // S'abonner au flux de notifications du service
        // this.notificationService.notifications$.pipe(
        //     takeUntil(this.destroy$)
        // ).subscribe(message => {
        //     // Le message reçu est déjà un objet parsé
        //     const notification: Notification = message.body ? JSON.parse(message.body) : message;

        //     console.log('Nouvelle notification reçue dans Header:', notification);

        //     // Ajouter la notification à la liste pour l'affichage
        //     this.messages.unshift(notification);

        //     // Optionnel: Limiter le nombre de notifications affichées
        //     if (this.messages.length > 10) {
        //         this.messages.pop();
        //     }

        //     this.snackBar.open(notification.message, 'OK', { duration: 5000 });
        // });
    }

        
    // clearAllNotifications(): void {
    //     this.messages = [];
    //     // Optionnel : vous pouvez aussi afficher un message de confirmation
    //     this.snackBar.open('Notifications nettoyées.', 'OK', { duration: 2000 });
    // }
   

    ngAfterViewInit() {
        if (isPlatformBrowser(this.platformId)) {
            document.addEventListener('fullscreenchange', this.onFullscreenChange.bind(this));
            document.addEventListener('webkitfullscreenchange', this.onFullscreenChange.bind(this));
            document.addEventListener('mozfullscreenchange', this.onFullscreenChange.bind(this));
            document.addEventListener('MSFullscreenChange', this.onFullscreenChange.bind(this));
        }
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
        if (isPlatformBrowser(this.platformId)) { // Nettoyer les écouteurs d'événements
            document.removeEventListener('fullscreenchange', this.onFullscreenChange.bind(this));
            document.removeEventListener('webkitfullscreenchange', this.onFullscreenChange.bind(this));
            document.removeEventListener('mozfullscreenchange', this.onFullscreenChange.bind(this));
            document.removeEventListener('MSFullscreenChange', this.onFullscreenChange.bind(this));
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
        return 'assets/images/logo.png'; // Image par défaut
    }

    logout() {
        this.authService.logout();
    }

    toggle() {
        this.toggleService.toggle();
    }

    settingsButtonToggle() {
        this.themeService.toggle();
    }

    toggleTheme() {
        this.themeService.toggleTheme();
    }

    @HostListener('window:scroll', ['$event'])
    checkScroll() {
        if (isPlatformBrowser(this.platformId)) { // Exécuter seulement côté navigateur
            const scrollPosition = window.scrollY || document.documentElement.scrollTop || document.body.scrollTop || 0;
            this.isSticky = scrollPosition >= 50;
        }
    }

    toggleFullscreen() {
        if (this.isFullscreen) {
            this.closeFullscreen();
        } else {
            this.openFullscreen();
        }
    }

    openFullscreen() {
        if (isPlatformBrowser(this.platformId)) {
            const element = document.documentElement as any; // Utiliser any pour simplifier l'accès aux méthodes préfixées
            if (element.requestFullscreen) {
                element.requestFullscreen();
            } else if (element.mozRequestFullScreen) {
                element.mozRequestFullScreen();
            } else if (element.webkitRequestFullscreen) {
                element.webkitRequestFullscreen();
            } else if (element.msRequestFullscreen) {
                element.msRequestFullscreen();
            }
        }
    }

    closeFullscreen() {
        if (isPlatformBrowser(this.platformId)) {
            const doc = document as any; // Utiliser any
            if (doc.exitFullscreen) {
                doc.exitFullscreen();
            } else if (doc.mozCancelFullScreen) {
                doc.mozCancelFullScreen();
            } else if (doc.webkitExitFullscreen) {
                doc.webkitExitFullscreen();
            } else if (doc.msExitFullscreen) {
                doc.msExitFullscreen();
            }
        }
    }

    onFullscreenChange() {
        if (isPlatformBrowser(this.platformId)) {
            const doc = document as any; // Utiliser any
            this.isFullscreen = !!(doc.fullscreenElement || doc.webkitFullscreenElement || doc.mozFullScreenElement || doc.msFullscreenElement);
        }
    }
}