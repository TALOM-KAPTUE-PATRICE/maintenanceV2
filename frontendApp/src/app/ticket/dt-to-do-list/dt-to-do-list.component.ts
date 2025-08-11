import { Component, inject, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TicketService } from '../TicketService/ticket.service';
import { User } from '../models/user.model';
import { Client } from '../models/client.model';
import { Ticket } from '../models/ticket.model';
import { TicketDetailsComponent } from '../ticket-details/ticket-details.component';
import { ClientService } from '../../client/client.service';
import { UserService } from '../../user.service.ts/user.service';
import { forkJoin } from 'rxjs';


@Component({
  selector: 'app-dt-to-do-list',
  standalone: false,
  templateUrl: './dt-to-do-list.component.html',
  styleUrls: ['./dt-to-do-list.component.scss']
})
export class DtToDoListComponent implements OnInit{

  // Table
  displayedColumns: string[] = ['id', 'titre', 'statut', 'avancement', 'userNom', 'clientNom', 'dateCreation', 'action'];
  dataSource = new MatTableDataSource<Ticket>([]);
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  
  // State Management
  isLoading = false;
  totalElements: number = 0;


  // Form
  ticketForm: FormGroup;
  isFormVisible = false;
  isEditing = false;
  editingTicketId: number | null = null;
  
  // Data for Selects
  clients: Client[] = [];
  users: User[] = [];

  private clientService = inject(ClientService);
  private userService = inject(UserService);


  constructor(
    private ticketService: TicketService,
    private fb: FormBuilder,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {
    this.ticketForm = this.fb.group({
      titre: ['', [Validators.required, Validators.maxLength(255)]],
      description: ['', Validators.required],
      clientId: [null, Validators.required],
      userId: [null, Validators.required]
      // Le statut et l'avancement sont gérés par le backend au début
    });
  }

  ngOnInit(): void {
    this.loadInitialData();
     this.loadTickets(); 
  }

   // CORRIGÉ : Logique de chargement simplifiée
  loadTickets(): void {
    this.isLoading = true;
    this.ticketService.getTickets().subscribe({
      next: (tickets) => {
        this.isLoading = false;
        this.dataSource.data = tickets;
        this.dataSource.paginator = this.paginator; // Lier le paginateur après avoir reçu les données
      },
      error: (err) => {
        this.isLoading = false;
        // L'erreur est déjà gérée par l'intercepteur, mais on peut logger ici si besoin.
        console.error("Erreur lors du chargement des tickets : ", err);
      }
    });
  }


  // NOUVELLE MÉTHODE : Charge tout en parallèle pour une meilleure expérience
  loadInitialData(): void {
    this.isLoading = true;
    forkJoin({
      tickets: this.ticketService.getTickets(),
      clients: this.clientService.getAllClients(),
      users: this.userService.getAllUsers()
    }).subscribe({
      next: ({ tickets, clients, users }) => {
        this.dataSource.data = tickets;
        this.dataSource.paginator = this.paginator;
        this.clients = clients;
        this.users = users;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        // L'intercepteur gère l'affichage, on logue juste.
        console.error("Erreur lors du chargement des données initiales", err);
      }
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  openForm(ticket?: Ticket): void {
    this.isFormVisible = true;
    if (ticket) {
      this.isEditing = true;
      this.editingTicketId = ticket.id;
      // Il faut charger les données complètes du ticket pour pré-remplir
      this.ticketService.getTicketById(ticket.id).subscribe(fullTicket => {
          // On doit trouver les IDs correspondants pour les selects
          const client = this.clients.find(c => c.nom === fullTicket.clientNom);
          const user = this.users.find(u => u.nom === fullTicket.userNom);
          this.ticketForm.patchValue({
              titre: fullTicket.titre,
              description: fullTicket.description,
              clientId: client?.id,
              userId: user?.id
          });
      });
    } else {
      this.isEditing = false;
      this.editingTicketId = null;
      this.ticketForm.reset();
    }
  }

  closeForm(): void {
    this.isFormVisible = false;
  }

  onSubmit(): void {
    if (this.ticketForm.invalid) {
      this.snackBar.open('Veuillez remplir tous les champs obligatoires.', 'Fermer', { duration: 3000 });
      return;
    }

    this.isLoading = true;
    const request = this.isEditing && this.editingTicketId
      ? this.ticketService.updateTicket(this.editingTicketId, this.ticketForm.value)
      : this.ticketService.createTicket(this.ticketForm.value);

    request.subscribe({
      next: () => {
        this.isLoading = false;
        const message = this.isEditing ? 'Ticket modifié avec succès !' : 'Ticket créé avec succès !';
        this.snackBar.open(message, 'OK', { duration: 3000 });
        this.closeForm();
        this.loadTickets(); // On recharge simplement toute la liste
      },
      error: (err) => {
        this.isLoading = false;
        this.snackBar.open(`Erreur: ${err.message}`, 'Fermer', { duration: 5000 });
      }
    });
  }

    /**
   * Recharge uniquement la liste des tickets, utile après une création/modification/suppression.
   */
  refreshTicketList(): void {
    this.isLoading = true;
    this.ticketService.getTickets().subscribe({
      next: (tickets) => {
        this.dataSource.data = tickets;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        console.error("Erreur lors du rafraîchissement des tickets", err);
      }
    });
  }


  onDelete(id: number): void {
    if (confirm("Êtes-vous sûr de vouloir supprimer ce ticket ?")) {
      this.isLoading = true;
      this.ticketService.deleteTicket(id).subscribe({
        next: () => {
          this.isLoading = false;
          this.snackBar.open('Ticket supprimé avec succès !', 'OK', { duration: 3000 });
          this.loadTickets(); // On recharge simplement toute la liste
        },
        error: (err) => {
          this.isLoading = false;
          this.snackBar.open(`Erreur lors de la suppression: ${err.message}`, 'Fermer', { duration: 5000 });
        }
      });
    }
  }
  
  onPrint(id: number): void {
      this.ticketService.getTicketPdf(id).subscribe(blob => {
          const fileURL = URL.createObjectURL(blob);
          window.open(fileURL, '_blank');
      });
  }

  onViewDetails(ticket: Ticket): void {
    this.dialog.open(TicketDetailsComponent, {
      data: ticket,
      width: '600px'
    });
  }
}