// src/app/shared/directives/has-permission.directive.ts (exemple de chemin)
import { Directive, Input, TemplateRef, ViewContainerRef, inject, OnDestroy } from '@angular/core';
import { AuthService } from '../../auth/service/auth.service'; // Ajustez le chemin
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Directive({
  selector: '[appHasPermission]',
  standalone: true
})
export class HasPermissionDirective implements OnDestroy {
  private authService = inject(AuthService);
  private templateRef = inject(TemplateRef<any>);
  private viewContainer = inject(ViewContainerRef);

  private requiredPermissions: string | string[] = [];
  private destroy$ = new Subject<void>();

  @Input()
  set appHasPermission(permissions: string | string[]) {
    this.requiredPermissions =  Array.isArray(permissions) ? permissions : [permissions];
    this.updateView();
  }

  constructor() {
    // S'abonner aux changements de l'utilisateur pour mettre à jour la vue si les permissions changent
    this.authService.currentUser$
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.updateView();
      });
  }

  private updateView(): void {
    if (this.authService.hasPermission(this.requiredPermissions)) {
      if (!this.viewContainer.length || this.viewContainer.length === 0) { // Créer la vue seulement si elle n'existe pas
        this.viewContainer.createEmbeddedView(this.templateRef);
      }
    } else {
      this.viewContainer.clear();
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}