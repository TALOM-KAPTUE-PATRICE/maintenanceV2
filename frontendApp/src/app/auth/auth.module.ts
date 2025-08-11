import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { AuthService } from './service/auth.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { IconsModule } from '../icons/icons.module';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';


const authRoutes: Routes = [
   { path: '', component: LoginComponent },
   { path: 'forgot-password', component: ForgotPasswordComponent},
   { path: 'reset-password', component: ResetPasswordComponent}
   
];


@NgModule({
  declarations: [  
    LoginComponent,
    ForgotPasswordComponent,
    ResetPasswordComponent
  ],

  imports: [
    CommonModule,
    RouterModule.forChild(authRoutes),
    ReactiveFormsModule,
    FormsModule,
    MatIconModule,
    RouterLink,
    MatFormFieldModule,
    MatCheckboxModule,
    MatIconModule,
    IconsModule,
    MatInputModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,     
  ],
  providers:[
    AuthService,
  ],
  exports:[
    RouterModule,
    ReactiveFormsModule,
    FormsModule,
    RouterLink,
    MatIconModule,
    MatFormFieldModule,
    MatCheckboxModule,
    MatInputModule,
    MatIconModule,
    IconsModule,

  ]

})
export class AuthModule { }
