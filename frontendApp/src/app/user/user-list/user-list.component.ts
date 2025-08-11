import { Component, ElementRef, inject, ViewChild } from '@angular/core';
import { environment } from '../../../environments/environment';
import { RoleApp, User } from '../models/user.model';
import { FormBuilder, FormGroup, Validators,  AbstractControl, ValidationErrors } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service';
import { UserService } from '../user.service';

// ▼▼▼ CORRECTION DU VALIDATEUR ▼▼▼
export function cameroonPhoneNumberValidator(): (control: AbstractControl) => ValidationErrors | null {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    if (!value) {
      return null;
    }
    // L'utilisateur doit taper un 6 suivi de 8 chiffres.
    // L'expression ^[6782] signifie "doit commencer par 6, 7, 8 ou 2".
    // L'expression \d{8}$ signifie "doit se terminer par exactement 8 chiffres".
    const cameroonPhoneRegex = /^[6]\d{8}$/;
    const isValid = cameroonPhoneRegex.test(value);
    // Le message d'erreur est aussi plus clair
    return isValid ? null : { invalidCameroonPhone: { message: 'Doit commencer par 6 et avoir 9 chiffres au total.' } };
  };
}

@Component({
  selector: 'app-user-list',
  standalone: false,
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.scss'
})
export class UserListComponent {

   
  // Injections
  public themeService = inject(CustomizerSettingsService);
  private userService = inject(UserService);
  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);
  hidePassword = true;
  @ViewChild('imageInput') imageInput!: ElementRef<HTMLInputElement>;

  // Table
  displayedColumns: string[] = ['image', 'nom', 'email', 'poste', 'role', 'action'];
  dataSource = new MatTableDataSource<User>();
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  // State
  isLoading = false;
  isFormVisible = false;
  isEditing = false;
  editingUserId: number | null = null;
  
  // Formulaire
  userForm: FormGroup;
  selectedFile: File | null = null;
  imagePreview: string | ArrayBuffer | null = null;

  // Constantes
  roles = Object.values(RoleApp);
  postes = ['DG', 'ASSISTANTE_DIRECTION', 'RESPONSABLE_COMMERCIAL', 'RESPONSABLE_ACHAT', 'RESPONSABLE_TECHNIQUE', 'TECHNICIEN_MAINTENANCE'];
  
  constructor() {
    this.userForm = this.fb.group({
      nom: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      numeroTelephone: ['', [Validators.required, cameroonPhoneNumberValidator()]],
      password: ['', [Validators.minLength(6)]],
      role: [RoleApp.USER, Validators.required],
      poste: ['', Validators.required],
      image: [null]
    });
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.isLoading = true;
    this.userService.getAll().subscribe({
      next: (users) => {
        this.dataSource.data = users;
        this.dataSource.paginator = this.paginator;
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  openForm(user?: User): void {
    this.isFormVisible = true;
    this.imagePreview = null;
    this.selectedFile = null;

    if (user) { // MODE ÉDITION
      this.isEditing = true;
      this.editingUserId = user.id;
      this.userForm.patchValue(user);

      // On rend le mot de passe optionnel en mode édition
      this.userForm.get('password')?.clearValidators();
      this.userForm.get('password')?.updateValueAndValidity();
      
      if(user.imagePath) {
        this.imagePreview = `${environment.apiUrl.replace('/api', '')}/${user.imagePath}`;
      } else {
        this.imagePreview = 'assets/images/reparer (1).png'; // Image par défaut si pas d'image
      }
    } else { // MODE CRÉATION
      this.isEditing = false;
      this.editingUserId = null;
      this.userForm.reset({ role: RoleApp.USER });
      
      // On rend le mot de passe obligatoire en mode création
      this.userForm.get('password')?.setValidators([Validators.required, Validators.minLength(6)]);
      this.userForm.get('password')?.updateValueAndValidity();

      this.imagePreview = 'assets/images/reparer (1).png'; // Image par défaut
           // ▼▼▼ CORRECTION : Réinitialiser la valeur de l'input de fichier ▼▼▼
      if (this.imageInput && this.imageInput.nativeElement) {
        this.imageInput.nativeElement.value = '';
      }
    }
  }


  closeForm(): void {
    this.isFormVisible = false;
  }

  onFileSelected(event: Event): void {
    const element = event.currentTarget as HTMLInputElement;
    const file = element.files?.[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = () => { this.imagePreview = reader.result; };
      reader.readAsDataURL(file);
    }
  }

  onSubmit(): void {
    if (this.userForm.invalid) {
      this.snackBar.open('Veuillez corriger les erreurs dans le formulaire.', 'Fermer', { duration: 3000 });
      return;
    }

    this.isLoading = true;
    const requestData = this.userForm.value;
    
    const request$ = this.isEditing && this.editingUserId
      ? this.userService.update(this.editingUserId, requestData, this.selectedFile ?? undefined)
      : this.userService.create(requestData, this.selectedFile ?? undefined);

    request$.subscribe({
      next: () => {
        const message = this.isEditing ? 'Utilisateur modifié avec succès !' : 'Utilisateur créé avec succès !';
        this.snackBar.open(message, 'OK', { duration: 3000 });
        this.closeForm();
        this.loadUsers();
      },
      error: () => this.isLoading = false
    });
  }
  
  onDelete(id: number): void {
      if (confirm("Êtes-vous sûr de vouloir supprimer cet utilisateur ? Cette action est irréversible.")) {
          this.userService.delete(id).subscribe({
              next: () => {
                  this.snackBar.open('Utilisateur supprimé avec succès !', 'OK', { duration: 3000 });
                  this.loadUsers();
              }
          });
      }
  }

  getUserImageUrl(path?: string): string {
    if (path) {
        return `${environment.apiUrl.replace('/api', '')}/${path}`;
    }
    return 'assets/images/reparer (1).png';
  }

}
