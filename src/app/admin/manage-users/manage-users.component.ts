import { Component, OnInit } from '@angular/core';
import { AdminService, ApiResponse } from 'src/app/Services/admin.service';

@Component({
  selector: 'app-manage-users',
  templateUrl: './manage-users.component.html',
  styleUrls: ['./manage-users.component.css']
})
export class ManageUsersComponent implements OnInit {

  users: any[] = [];
  successMessage: string = ''; // to show messages

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  // loadUsers() {
  //   this.adminService.getAllUsers().subscribe(res => this.users = res);
  // }
loadUsers() {
  this.adminService.getAllUsers().subscribe({
    next: res => {
      console.log('Users from API:', res.users); // 👈 add this
      this.users = [...res.users]; // 👈 make a new array
    },
    error: err => console.error('Error loading users:', err)
  });
}

  deleteUser(id: number) {
    this.adminService.deleteUser(id).subscribe((res: ApiResponse) => {
      this.successMessage = res.message; // show success
      this.loadUsers();
      this.clearMessageAfterDelay();
    });
  }

  changeRole(id: number) {
    this.adminService.changeUserRole(id, 'ROLE_ADMIN').subscribe((res: ApiResponse) => {
      this.successMessage = res.message; // show success
      this.loadUsers();
      this.clearMessageAfterDelay();
    });
  }
  
toggleStatus(user: any) {
  this.adminService.updateUserStatus(user.id, !user.active).subscribe(res => {
    this.successMessage = res.message;
    this.loadUsers();
    this.clearMessageAfterDelay();
  });
}

  private clearMessageAfterDelay() {
    setTimeout(() => this.successMessage = '', 3000); // clear after 3s
  }
}


