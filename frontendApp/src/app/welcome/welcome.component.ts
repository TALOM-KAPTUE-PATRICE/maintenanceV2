// src/app/welcome/welcome.component.ts

import { Component, OnInit, OnDestroy, AfterViewInit, ElementRef, ViewChildren, QueryList, HostListener } from '@angular/core'; // Ajoutez HostListener
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { Router } from '@angular/router';
import { CustomizerSettingsService } from '../home/dashboard/customizer-settings/customizer-settings.service';

interface Solution {
  image: string;
  title: string;
  description: string;
}

// Nouvelle interface pour les slides du grand carousel d'accueil
interface HeroSlide {
  image: string;
  title: string;
  description: string;
}

@Component({
  selector: 'app-welcome',
  standalone: false,
  templateUrl: './welcome.component.html',
  styleUrl: './welcome.component.scss'
})

export class WelcomeComponent implements OnInit, OnDestroy, AfterViewInit {
  // Propriétés du premier carousel (solutions) - Existant
  private autoSlideInterval: any;
  private readonly SLIDE_DURATION_MS = 5000;
  currentSlideIndex: number = 0;
  solutions: Solution[] = [
    {
      image: 'assets/images/undraw_real-time-analytics_50za.svg',
      title: 'Suivi en Temps Réel',
      description: 'Visualisez l\'état de vos équipements, les interventions en cours et les alertes en direct pour une réactivité maximale.'
    },
    {
      image: 'assets/images/undraw_qa-engineers_kgp8.svg',
      title: 'Gestion des Interventions',
      description: 'Planifiez, attribuez et suivez les ordres de travail de maintenance avec efficacité, du diagnostic à la résolution.'
    },
    {
      image: 'assets/images/undraw_predictive-analytics_6vi1.svg',
      title: 'Maintenance Préventive & Prédictive',
      description: 'Anticipez les pannes grâce à l\'analyse de données et mettez en place des calendriers de maintenance optimisés.'
    },
    {
      image: 'assets/images/undraw_stock-prices_8nuz.svg',
      title: 'Gestion de Stocks Pièces Détachées',
      description: 'Optimisez vos inventaires, suivez les consommations et automatisez les commandes pour éviter les ruptures.'
    },
    {
      image: 'assets/images/undraw_all-the-data_5lil.svg',
      title: 'Reporting et Analyse Avancée',
      description: 'Obtenez des insights précieux sur vos opérations de maintenance avec des tableaux de bord personnalisables.'
    }
  ];

  // NOUVEAU: Propriétés du grand carousel d'accueil
  private heroAutoSlideInterval: any;
  private readonly HERO_SLIDE_DURATION_MS = 8000; // 8 secondes pour ce carousel
  currentHeroSlideIndex: number = 0;
  heroSlides: HeroSlide[] = [
    {
      image: 'assets/images/undraw_loading_3kqt.svg', // Remplacez par vos images
      title: 'Maintenance Intelligente',
      description: 'Optimisez vos opérations avec des outils de suivi et d\'analyse de pointe.'
    },
    {
      image: 'assets/images/undraw_real-time-collaboration_g4mc.svg',
      title: 'Équipes Connectées, Opérations Fluidifiées',
      description: 'Collaborez en temps réel et gérez les interventions sur le terrain avec aisance.'
    },
    {
      image: 'assets/images/undraw_visualization_zhab.svg',
      title: 'Performance Industrielle à Votre Portée',
      description: 'Réduisez les temps d\'arrêt et augmentez la durée de vie de vos équipements.'
    }
  ];

  // Propriétés pour l'Intersection Observer (existant)
  private observer!: IntersectionObserver;
  @ViewChildren('animatedSection') animatedSections!: QueryList<ElementRef>;
  @ViewChildren('statItem') statItems!: QueryList<ElementRef>;
  @ViewChildren('testimonialCard') testimonialCards!: QueryList<ElementRef>;
  @ViewChildren('securityInfo') securityInfo!: QueryList<ElementRef>;
  @ViewChildren('supportInfo') supportInfo!: QueryList<ElementRef>;

  // NOUVEAU: Propriété pour le bouton "Retour en haut"
  showScrollToTopButton: boolean = false;


  constructor(private router: Router, public themeService: CustomizerSettingsService) { }

  ngOnInit(): void {
    this.startAutoSlide(); // Lance le carousel des solutions
    this.startHeroAutoSlide(); // NOUVEAU: Lance le grand carousel d'accueil
  }

  ngAfterViewInit(): void {
    // Initialiser l'Intersection Observer après que la vue est rendue
    this.initIntersectionObserver();

    // Observer les sections principales
    this.animatedSections.forEach(section => {
      this.observer.observe(section.nativeElement);
    });

    // Observer les éléments spécifiques dans les sections (pour des animations séquentielles)
    this.statItems.forEach(item => {
      this.observer.observe(item.nativeElement);
    });
    this.testimonialCards.forEach(card => {
      this.observer.observe(card.nativeElement);
    });
    this.securityInfo.forEach(item => {
      this.observer.observe(item.nativeElement);
    });
    this.supportInfo.forEach(item => {
      this.observer.observe(item.nativeElement);
    });
  }

  ngOnDestroy(): void {
    this.stopAutoSlide();
    this.stopHeroAutoSlide(); // NOUVEAU: Arrête le grand carousel d'accueil
    if (this.observer) {
      this.observer.disconnect();
    }
  }

  // --- Méthodes du premier Carousel (Solutions) - Existant ---
  startAutoSlide(): void {
    this.autoSlideInterval = setInterval(() => {
      this.nextSlide();
    }, this.SLIDE_DURATION_MS);
  }

  stopAutoSlide(): void {
    if (this.autoSlideInterval) {
      clearInterval(this.autoSlideInterval);
    }
  }

  nextSlide(): void {
    this.currentSlideIndex = (this.currentSlideIndex + 1) % this.solutions.length;
    this.resetAutoSlide();
  }

  prevSlide(): void {
    this.currentSlideIndex = (this.currentSlideIndex - 1 + this.solutions.length) % this.solutions.length;
    this.resetAutoSlide();
  }

  goToSlide(index: number): void {
    this.currentSlideIndex = index;
    this.resetAutoSlide();
  }

  private resetAutoSlide(): void {
    this.stopAutoSlide();
    this.startAutoSlide();
  }

  goToLogin(): void {
    this.router.navigate(['/auth']);
  }

  // --- NOUVEAU: Méthodes du grand Carousel d'accueil ---
  startHeroAutoSlide(): void {
    this.heroAutoSlideInterval = setInterval(() => {
      this.nextHeroSlide();
    }, this.HERO_SLIDE_DURATION_MS);
  }

  stopHeroAutoSlide(): void {
    if (this.heroAutoSlideInterval) {
      clearInterval(this.heroAutoSlideInterval);
    }
  }

  nextHeroSlide(): void {
    this.currentHeroSlideIndex = (this.currentHeroSlideIndex + 1) % this.heroSlides.length;
    this.resetHeroAutoSlide();
  }

  prevHeroSlide(): void {
    this.currentHeroSlideIndex = (this.currentHeroSlideIndex - 1 + this.heroSlides.length) % this.heroSlides.length;
    this.resetHeroAutoSlide();
  }

  goToHeroSlide(index: number): void {
    this.currentHeroSlideIndex = index;
    this.resetHeroAutoSlide();
  }

  private resetHeroAutoSlide(): void {
    this.stopHeroAutoSlide();
    this.startHeroAutoSlide();
  }


  // --- Méthodes d'Intersection Observer (existant) ---
  private initIntersectionObserver(): void {
    const options = {
      root: null,
      rootMargin: '0px',
      threshold: 0.1
    };

    this.observer = new IntersectionObserver((entries, observer) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          entry.target.classList.add('is-visible');
        }
      });
    }, options);
  }

  // NOUVEAU: Méthodes pour le bouton "Retour en haut"
  @HostListener('window:scroll', ['$event'])
  onScroll(): void {
    // Affiche le bouton si le défilement est supérieur à 300px
    this.showScrollToTopButton = (window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0) > 300;
  }

  scrollToTop(): void {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
}