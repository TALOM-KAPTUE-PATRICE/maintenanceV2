import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DevisService } from '../devisService/devis.service';
import { Article, CreateArticleRequest } from '../model/article.model'; // Importez votre modèle d'article
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Categorie } from '../../models/categorie.model';
import { MatTableDataSource } from '@angular/material/table';
import { CustomizerSettingsService } from '../../home/dashboard/customizer-settings/customizer-settings.service';
import { MatPaginator } from '@angular/material/paginator';
import { CategorieService } from '../../categorie/categorie.service';
import { ArticleService } from '../../article/article.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-article',
  standalone: false,
  templateUrl: './article.component.html',
  styleUrl: './article.component.scss'
})
export class ArticleComponent implements OnInit {

  form: FormGroup;
  devisId: string= ""; // Récupérez l'ID du devis ici
  articles: Article[] = [];
  categories: Categorie[] = [];
  dataSource = new MatTableDataSource<Article>();
  displayedColumns: string[] = ['designation', 'quantite', 'prixUnitaire' , 'action'];
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private fb: FormBuilder,
    private devisService: DevisService,
    private articleService: ArticleService, 
    private snackBar: MatSnackBar,      
    private categorieService: CategorieService, // <-- INJECTION
    public themeService: CustomizerSettingsService,
    private dialogRef: MatDialogRef<ArticleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
    // Injectez les données du dialog ici
  ) {
    this.form = this.fb.group({
      designation: ['', Validators.required],
      quantite: [0, Validators.required],
      prixUnitaire: [0, Validators.required],
      categorie: ['', Validators.required]
    });

    this.devisId = data.devisId; // Récupérez l'ID du devis

  }

  applyFilter(event: KeyboardEvent): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  ngOnInit() {
    this.loadArticles();
    this.loadCategories();
  }
  
  close(){
    this.dialogRef.close();
  }


  loadCategories() {
    // ▼▼▼ UTILISEZ LE NOUVEAU SERVICE ▼▼▼
    this.categorieService.getAllCategories().subscribe(data => {
      this.categories = data;
      console.log("Data categorie", data);
    });
  }
  

  loadArticles() {
    this.devisService.getArticlesForDevis(this.devisId).subscribe(data => {
      this.dataSource.data = data;
      this.dataSource.paginator = this.paginator;
    });
  }

 viewArticle(id: number){}
 editArticle(id: number ){}
 deleteArticle(id: number){}

   // ▼▼▼ MÉTHODE MANQUANTE 1 ▼▼▼
  loadArticlesForDevis(devisId: string): void {
    // Cette méthode recharge la liste des articles déjà associés au devis
    this.devisService.getArticlesForDevis(devisId).subscribe(articles => {
      this.articles = articles; // Mettre à jour le tableau des articles affichés
    });
  }

  // ▼▼▼ MÉTHODE MANQUANTE 2 ▼▼▼
  openSnackBar(message: string, action: string): void {
    this.snackBar.open(message, action, {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom'
    });
  }

  // ▼▼▼ MÉTHODE MANQUANTE 3 ▼▼▼
  resetForm(): void {
    this.form.reset({
      quantite: 1, // Valeur par défaut
      prixUnitaire: 0 // Valeur par défaut
    });
    // On ne réinitialise pas la catégorie pour faciliter les ajouts multiples
  }


createOrUpdateArticle() {
        if (this.form.valid) {
            // ▼▼▼ CORRECTION DE LA LOGIQUE ▼▼▼
            const articleRequest: CreateArticleRequest = {
                designation: this.form.value.designation,
                quantite: this.form.value.quantite,
                prixUnitaire: this.form.value.prixUnitaire,
                categorieId: this.form.value.categorie
            };

            // Étape 1 : Créer l'article
            this.articleService.createArticle(articleRequest).subscribe(createdArticle => {
                console.log('Article créé avec ID:', createdArticle.id);
                // Étape 2 : Ajouter l'article créé au devis
                if (createdArticle && createdArticle.id) {
                    this.devisService.addArticleToDevis(this.devisId, createdArticle.id).subscribe(() => {
                        this.loadArticlesForDevis(this.devisId); // Rafraîchir la liste
                        this.openSnackBar('Article ajouté au devis avec succès !', 'OK');
                        this.resetForm();
                    });
                }
            });
        }
    }
}
