// // import { Injectable } from '@angular/core';
// // import { HttpClient, HttpHeaders } from '@angular/common/http';
// // import { JwtHelperService } from '@auth0/angular-jwt';
// // import { Observable } from 'rxjs';
// // import { jwtDecode } from 'jwt-decode';

// // @Injectable({
// //   providedIn: 'root'
// // })
// // export class AuthService {

// //   private baseUrl = 'http://localhost:8080/auth';
  

// //   constructor(private http: HttpClient, private jwtHelper: JwtHelperService) {}

// //   // Register user
// //   register(user: any): Observable<any> {
// //     return this.http.post(`${this.baseUrl}/register`, user);
// //   }

// //   // Login user
// //   login(credentials: any): Observable<any> {
// //     return this.http.post(`${this.baseUrl}/login`, credentials);
// //   }

// //   // Set token to localStorage
// //  setToken(token: string) {
// //   localStorage.setItem('token', token);

// //     const decoded: any = this.jwtHelper.decodeToken(token);
// //     if (decoded) {
// //       localStorage.setItem('role', decoded.role);
// //       localStorage.setItem('email', decoded.sub);       // email from token
// //       localStorage.setItem('username', decoded.username || 'User');
// //     }
// // }


// //   // Get token from localStorage
// //   getToken() {
// //     return localStorage.getItem('token');
// //   }

// //   // Check login status
// //   isLoggedIn(): boolean {
// //     const token = this.getToken();
// //     return token != null && !this.jwtHelper.isTokenExpired(token);
// //   }

// //   // Remove token for logout
// //   logout() {
// //     localStorage.removeItem('token');
// //     localStorage.removeItem('username');
// //   }

// //   // Get user role from token
// //   getUserRole(): string {
// //     const token = this.getToken();
// //     if (token) {
// //       const decoded = this.jwtHelper.decodeToken(token);
// //       return decoded?.role || '';
// //     }
// //     return '';
// //   }

// //   // Get user email from token
// //   getUserEmail(): string {
// //     const token = this.getToken();
// //     if (token) {
// //       const decodedToken = this.jwtHelper.decodeToken(token);
// //       return decodedToken.sub;
// //     }
// //     return '';
// //   }

  

// //   getUsername(): string {
// //   const token = this.getToken();
// //   if (token) {
// //     const decoded:any = jwtDecode(token)
// //     console.log(decoded)
// //     return decoded.username || 'User';
// //   }
// //   return 'User';
// // }

// // }
// import { Injectable } from '@angular/core';
// import { HttpClient } from '@angular/common/http';
// import { JwtHelperService } from '@auth0/angular-jwt';
// import { Observable } from 'rxjs';
// import { jwtDecode } from 'jwt-decode';


// @Injectable({
//   providedIn: 'root'
// })
// export class AuthService {

//   private baseUrl = 'http://localhost:8080/auth';

//   constructor(private http: HttpClient, private jwtHelper: JwtHelperService) {}

//   // 🔹 Register user
//   register(user: any): Observable<any> {
//     return this.http.post(`${this.baseUrl}/register`, user);
//   }

//   // 🔹 Login user
//   login(credentials: any): Observable<any> {
//     return this.http.post(`${this.baseUrl}/login`, credentials);
//   }

//   // 🔹 Save token & store decoded values
//   setToken(token: string) {
//     localStorage.setItem('token', token);

//     const decoded: any = this.jwtHelper.decodeToken(token);
//     if (decoded) {
//       localStorage.setItem('role', decoded.role);
//       localStorage.setItem('email', decoded.sub);       // email
//       localStorage.setItem('username', decoded.username || 'User');
//     }
//   }

//   // 🔹 Get token
//   getToken(): string | null {
//     return localStorage.getItem('token');
//   }

//   // 🔹 Check if logged in
//   isLoggedIn(): boolean {
//     const token = this.getToken();
//     return token != null && !this.jwtHelper.isTokenExpired(token);
//   }

//   // 🔹 Logout
//   logout() {
//     localStorage.removeItem('token');
//     localStorage.removeItem('username');
//     localStorage.removeItem('role');
//     localStorage.removeItem('email');
//   }

//   // 🔹 Get role
//   getUserRole(): string {
//     const token = this.getToken();
//     if (token) {
//       const decoded: any = this.jwtHelper.decodeToken(token);
//       return decoded?.role || '';
//     }
//     return '';
//   }

//   // 🔹 Get email
//   getUserEmail(): string {
//     const token = this.getToken();
//     if (token) {
//       const decoded: any = this.jwtHelper.decodeToken(token);
//       return decoded?.sub || '';
//     }
//     return '';
//   }

//   // 🔹 Get username
// //   getUsername(): string {
// //     const token = this.getToken();
// //     if (token) {
// //       const decoded: any = jwtDecode(token); // ✅ consistent decoding
// //       return decoded?.username || 'User';
// //     }
// //     return 'User';
// //   }
// // }
// getUsername(): string {
//   const token = this.getToken();
//   if (token) {
//     const decoded: any = this.jwtHelper.decodeToken(token);
//     return decoded?.username || 'User';
//   }
//   return 'User';
// }
// }





import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable, BehaviorSubject } from 'rxjs';  // Added BehaviorSubject for reactive user info

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:8080/auth';
  private jwtHelper = new JwtHelperService();  // Instantiate once for reuse
  private currentUserSubject = new BehaviorSubject<any>(null);  // Optional: Reactive user state

  constructor(private http: HttpClient) {
    // Load user from storage on init (e.g., after refresh)
    const token = this.getToken();
    if (token) {
      this.setToken(token);  // Re-decode and set
    }
  }

  // 🔹 Register user
  register(user: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, user);
  }

  // 🔹 Login user
  login(credentials: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, credentials);
  }

  // 🔹 Save token & store decoded values
  setToken(token: string): void {
    if (!token || this.jwtHelper.isTokenExpired(token)) {
      console.warn('Invalid or expired token provided');
      return;
    }

    localStorage.setItem('token', token);

    try {
      const decoded: any = this.jwtHelper.decodeToken(token);
      if (decoded) {
        localStorage.setItem('role', decoded.role || '');
        localStorage.setItem('email', decoded.sub || '');  // email as subject
        localStorage.setItem('username', decoded.username || 'User');
        // Optional: Emit current user
        this.currentUserSubject.next(decoded);
      }
    } catch (error) {
      console.error('Failed to decode token:', error);
      this.logout();  // Clean up on error
    }
  }

  // 🔹 Get token
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // 🔹 Check if logged in
  isLoggedIn(): boolean {
    const token = this.getToken();
    return token != null && !this.jwtHelper.isTokenExpired(token);
  }

  // 🔹 Logout
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
    localStorage.removeItem('email');
    this.currentUserSubject.next(null);  // Reset reactive state
  }

  // 🔹 Get role
  getUserRole(): string {
    const token = this.getToken();
    if (token) {
      try {
        const decoded: any = this.jwtHelper.decodeToken(token);
        return decoded?.role || '';
      } catch (error) {
        console.error('Failed to decode role:', error);
      }
    }
    return '';
  }

  // 🔹 Get email
  getUserEmail(): string {
    const token = this.getToken();
    if (token) {
      try {
        const decoded: any = this.jwtHelper.decodeToken(token);
        return decoded?.sub || '';
      } catch (error) {
        console.error('Failed to decode email:', error);
      }
    }
    return '';
  }

  // 🔹 Get username
  getUsername(): string {
    const token = this.getToken();
    if (token) {
      try {
        const decoded: any = this.jwtHelper.decodeToken(token);
        return decoded?.username || 'User';
      } catch (error) {
        console.error('Failed to decode username:', error);
      }
    }
    return 'User';
  }

  // 🔹 Get current user ID (for ActivityComponent; adjust claim name if needed)
  getCurrentUserId(): number | null {
    const token = this.getToken();
    if (token) {
      try {
        const decoded: any = this.jwtHelper.decodeToken(token);
        return decoded?.id || decoded?.userId || null;  // Assumes 'id' or 'userId' in payload
      } catch (error) {
        console.error('Failed to decode user ID:', error);
      }
    }
    return null;
  }

  // 🔹 Optional: Reactive current user observable
  getCurrentUser(): Observable<any> {
    return this.currentUserSubject.asObservable();
  }

  
}