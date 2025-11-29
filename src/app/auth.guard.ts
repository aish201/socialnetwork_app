import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from './Services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot, 
    state: RouterStateSnapshot
  ): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
    
    // Allow access to logout page without token
    if (state.url === '/logout') {
      return true;
    }

    if (this.authService.isLoggedIn()) {
      return true;  // user is logged in, allow route
    } else {
      // user not logged in, redirect to login page
      this.router.navigate(['/login']);
      return false;
    }
  }
}

