import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './Services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    const role =localStorage.getItem('role')
    // Check if logged in and role is ADMIN
    if (this.authService.isLoggedIn() && role === 'ROLE_ADMIN') {
      return true;
    }

    // If not, redirect to dashboard
    this.router.navigate(['/dashboard']);
    return false;
  }
}

