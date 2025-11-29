import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../Services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  user = {
    username: '',   // <-- changed from 'name'
    email: '',
    password: ''
  };
  registerRequest = {
  username: '',
  email: '',
  password: '',
  role: 'ROLE_USER'  // default
};


  constructor(private authService: AuthService, private router: Router) {}

  register() {
    this.authService.register(this.user).subscribe(
      (res: any) => {
        alert('Registration Successful!');
        this.router.navigate(['/login']);
      },
      (err) => {
        console.log(err)
        alert('Registration Failed!');
      }
    );
  }
}


