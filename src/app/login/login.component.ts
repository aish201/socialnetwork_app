// import { Component } from '@angular/core';
// import { AuthService } from '../Services/auth.service';
// import { Router } from '@angular/router';

// @Component({
//   selector: 'app-login',
//   templateUrl: './login.component.html',
//   styleUrls: ['./login.component.css']
// })
// export class LoginComponent {
//   credentials = {
//     email: '',
//     password: ''
//   };

//   constructor(private authService: AuthService, private router: Router) {}

//   login() {
//     this.authService.login(this.credentials).subscribe(
//       (res: any) => {
//         this.authService.setToken(res.token);               // ✅ use AuthService method
//         localStorage.setItem('username', this.credentials.email); // optionally store username
//         localStorage.setItem('role',res.role)
//         console.log("login role ",res)
//         if(res.role=='ROLE_ADMIN'){
//           this.router.navigate(['/admin/dashboard']);
//         }else{
//           this.router.navigate(['/dashboard']);
//         }
//       },
//       (err) => {
//         alert('Invalid Credentials!');
//       }
//     );
//   }
// }
// import { Component } from '@angular/core';
// import { AuthService } from '../Services/auth.service';
// import { Router } from '@angular/router';
// import { NotificationService } from '../Services/notification.service';

// @Component({
//   selector: 'app-login',
//   templateUrl: './login.component.html',
//   styleUrls: ['./login.component.css']
// })
// export class LoginComponent {
//   credentials = {
//     email: '',
//     password: ''
//   };

//   constructor(
//     private authService: AuthService,
//     private router: Router,
//     private notificationService: NotificationService // ✅ add this
//   ) {}

//   login() {
//     this.authService.login(this.credentials).subscribe(
//       (res: any) => {
//         this.authService.setToken(res.token);
//         localStorage.setItem('username', this.credentials.email);
//         localStorage.setItem('role', res.role);
//         console.log("login role ", res);

//         // ✅ Initialize notifications after login
//         this.notificationService.init();

//         if (res.role === 'ROLE_ADMIN') {
//           this.router.navigate(['/admin/dashboard']);
//         } else {
//           this.router.navigate(['/dashboard']);
//         }
//       },
//       (err) => {
//         alert('Invalid Credentials!');
//       }
//     );
//   }
// }
import { Component } from '@angular/core';
import { AuthService } from '../Services/auth.service';
import { Router } from '@angular/router';
import { NotificationService } from '../Services/notification.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  credentials = {
    email: '',
    password: ''
  };

  constructor(
    private authService: AuthService,
    private notificationService: NotificationService,
    private router: Router
  ) {}

  login() {
    this.authService.login(this.credentials).subscribe(
      (res: any) => {
        // Store token and username
        this.authService.setToken(res.token);
        localStorage.setItem('username', this.credentials.email);
        localStorage.setItem('role', res.role);

        // Initialize WebSocket connection for notifications
        this.notificationService.init();

        console.log('Login successful:', res);
        if (res.role === 'ROLE_ADMIN') {
          this.router.navigate(['/admin/dashboard']);
        } else {
          this.router.navigate(['/dashboard']);
        }
      },
      (err) => {
        alert('Invalid Credentials!');
      }
    );
  }
}
