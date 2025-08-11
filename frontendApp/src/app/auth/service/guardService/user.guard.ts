import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class UserGuard implements CanActivate {
  
  constructor(private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {
    
    const user = JSON.parse(sessionStorage.getItem('user') || '{}');
    
    if (user && user.role === 'USER') {
      return true; // Accès autorisé
    } else if(user.role === 'ADMIN'){
      this.router.navigate(['/admin']);
      return false; // Accès refusé
    }else if(user.role === 'SUPER_ADMIN'){
        this.router.navigate(['/super-admin'])
        return false;
    }else{
        return false;
    }
  }
}