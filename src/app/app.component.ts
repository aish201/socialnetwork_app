import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NotificationService } from './Services/notification.service';
import { UserService } from './Services/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  unreadCount: number = 0;

  searchText: string = '';
  filteredUsers: any[] = [];

   keyword: string = "";
  constructor(
    private router: Router,
    private notificationService: NotificationService,
    private userService: UserService
  ) {}

  ngOnInit() {
    const token = localStorage.getItem('token');
    if (token) {
      this.notificationService.init();
    }

    this.notificationService.getNotifications().subscribe((notifs) => {
      this.unreadCount = notifs.filter(n => !n.readStatus).length;
    });
  }

  isAuthPage(): boolean {
    const authRoutes = ['/login', '/register'];
    return authRoutes.includes(this.router.url);
  }

  get isAdmin(): boolean {
    const role = localStorage.getItem('role');
    return role === 'ROLE_ADMIN';
  }

  logout(): void {
    localStorage.clear();
    this.notificationService.clearNotifications();
    this.router.navigate(['/login']);
  }

  clearBadge(): void {
    this.unreadCount = 0;
  }

  // 🔥 Search while typing
  onSearchChange() {
    if (!this.searchText.trim()) {
      this.filteredUsers = [];
      return;
    }

    this.userService.searchUsers(this.searchText).subscribe(
      (users) => {
        this.filteredUsers = users;
        console.log("Search results:", users);
      },
      (error) => {
        console.error("Search error:", error);
      }
    );
  }

  searchUsers() {
  if (this.keyword.trim().length < 1) {
    this.filteredUsers = [];
    return;
  }

  this.userService.searchUsers(this.keyword).subscribe(
    (users: any[]) => {
      this.filteredUsers = users.map(u => ({
        username: u.username,
        email: u.email,
        profilePicUrl: u.profilePicUrl
      }));
    },
    error => {
      console.error("Search error:", error);
    }
  );
}

selectSearchedUser(user: any) {
  this.keyword = "";
  this.filteredUsers = [];

  this.router.navigate(['/user-profile', user.email]);
}
openFirstUser() {
  if (this.filteredUsers.length > 0) {
    const user = this.filteredUsers[0];

    this.keyword = "";
    this.filteredUsers = [];

    this.router.navigate(['/user-profile', user.email]);
  }
}

}
