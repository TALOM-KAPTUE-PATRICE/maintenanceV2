import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotFoundComponent } from './not-found/not-found.component';
import { authGuard } from './auth/service/guardService/auth.guard';
import { loginGuard } from './auth/service/guardService/login.guard';


const routes: Routes = [  

  { path: 'auth', loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule), canActivate: [loginGuard]  },
  { path: 'home', loadChildren: () => import('./home/home.module').then(m => m.HomeModule), canActivate: [authGuard] }, //, canActivate: [AuthGuard, UserGuard]
  { 
    path: '', 
    loadChildren: () => import('./landing/landing.module').then(m => m.LandingModule) 
  },
  { path: '**', component: NotFoundComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }
