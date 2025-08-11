import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { OwlOptions } from 'ngx-owl-carousel-o';
 

@Component({
  selector: 'app-landing-page',
  standalone: false,
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss']
})
export class LandingPageComponent {

 
  public _currentYear: number = new Date().getFullYear();
  // Pour la FAQ
  panelOpenState = false;

    // ▼▼▼ NOUVEAU : Images pour le carrousel du dashboard ▼▼▼
  dashboardImages = [
    { id: '1', src: 'assets/images/dashboard.png', alt: 'Vue des KPIs' },
    { id: '2', src: 'assets/images/dashboard1.png', alt: 'Liste des Projets' },
    { id: '3', src: 'assets/images/dashboard2.png', alt: 'Formulaire de création' }
  ];

  // ▼▼▼ NOUVEAU : Options pour le carrousel du dashboard ▼▼▼
  dashboardCarouselOptions: OwlOptions = {
    items: 1,
    loop: true,
    mouseDrag: true,
    touchDrag: true,
    pullDrag: true,
    dots: true,
    navSpeed: 700,
    navText: ['<i class="ri-arrow-left-s-line"></i>', '<i class="ri-arrow-right-s-line"></i>'],
    autoplay: true,
    autoplayTimeout: 2000,
    autoplayHoverPause: true,
    nav: true // Affiche les flèches de navigation
  };


  // Données pour la page (pourrait venir d'un service plus tard)
  features = [
    { icon: 'receipt_long', title: 'Gestion de Projets (Tickets)', description: 'Suivez chaque intervention, de la création à la clôture, avec un statut et un avancement clairs.' },
    { icon: 'request_quote', title: 'Devis & Facturation', description: 'Générez des devis professionnels et des factures en quelques clics, basés sur vos articles et prestations.' },
    { icon: 'inventory_2', title: 'Catalogue d\'Articles', description: 'Gérez votre stock de pièces et de services pour des chiffrages rapides et précis.' },
    { icon: 'people', title: 'Suivi Client & Fournisseur', description: 'Centralisez toutes les informations sur vos clients et fournisseurs pour des relations commerciales fluides.' },
    { icon: 'shopping_cart', title: 'Processus d\'Achat', description: 'Rationalisez vos achats internes avec un système de demandes et de bons de commande.' },
    { icon: 'bar_chart', title: 'Tableaux de Bord (KPIs)', description: 'Prenez des décisions éclairées grâce à des indicateurs de performance visuels et en temps réel.' },
  ];

  testimonials = [
    { name: 'Jean B.', company: 'SABC', quote: '"Maint-Pro a transformé notre suivi de maintenance. Nous avons réduit nos temps d\'intervention de 30% !"', image: 'assets/images/user1.svg' },
    { name: 'Aïcha K.', company: 'SOCAVER', quote: '"La gestion des devis et des factures est devenue un jeu d\'enfant. Une visibilité totale sur notre activité."', image: 'assets/images/user2.svg' },
    { name: 'Paul M.', company: 'CIMENCAM', quote: '"Enfin un outil adapté à la réalité de la maintenance industrielle au Cameroun. Simple et puissant."', image: 'assets/images/user3.svg' },
  ];

    // ▼▼▼ NOUVEAU : Options pour le carrousel des témoignages ▼▼▼
  testimonialCarouselOptions: OwlOptions = {
    loop: true,
    mouseDrag: true,
    touchDrag: true,
    pullDrag: false,
    dots: true,
    navSpeed: 700,
    navText: ['', ''],
    responsive: {
      0: { items: 1 },
      768: { items: 2 },
      992: { items: 3 }
    },
    nav: false,
    // --- AJOUTEZ CES DEUX LIGNES ---
    autoplay: true,                      // Active le défilement automatique
    autoplayTimeout: 1000,               // Délai en millisecondes (4 secondes) entre chaque diapositive
    autoplayHoverPause: true             // Met le carrousel en pause lorsque la souris est dessus
  };

  faq = [
    { question: 'L\'application est-elle sécurisée ?', answer: 'Oui, Maint-Pro utilise un cryptage de bout en bout, une authentification robuste par tokens JWT et une gestion fine des permissions pour garantir la sécurité de vos données.' },
    { question: 'Puis-je utiliser l\'application sur mobile ?', answer: 'Absolument. Notre interface est entièrement responsive et s\'adapte parfaitement aux smartphones et tablettes pour un suivi sur le terrain.'  },
    { question: 'Proposez-vous une assistance technique ?', answer: 'Oui, tous nos abonnements incluent une assistance technique par email et téléphone pour vous accompagner au quotidien.' },
    { question: 'La migration de mes données existantes est-elle possible ?', answer: 'Nous proposons des services d\'accompagnement pour importer vos données existantes (clients, articles) afin de démarrer rapidement.' }
  ];

  constructor(private router: Router) {}

  navigateToLogin() {
    this.router.navigate(['/auth']);
  }
}