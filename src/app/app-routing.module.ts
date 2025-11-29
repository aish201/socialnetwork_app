import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ProfileComponent } from './profile/profile.component';
import { AdminDashboardComponent } from './admin/admin-dashboard/admin-dashboard.component';
import { ManageUsersComponent } from './admin/manage-users/manage-users.component';
import { ManagePostsComponent } from './admin/manage-posts/manage-posts.component';
import { ManageCommentsComponent } from './admin/manage-comments/manage-comments.component';
import { AuthGuard } from './auth.guard';
import { AdminGuard } from './admin.guard';
import { UserSearchComponent } from './user-search/user-search.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ActivityComponent } from './activity/activity.component';
import { ReportIssueComponent } from './report-issue/report-issue.component';
import { AdminComplaintsComponent } from './admin/admin-complaints/admin-complaints.component';
import { UserComplaintsComponent } from './user-complaints/user-complaints.component';
const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },  // 👈 go to login by default

  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },

  { path: 'admin/dashboard', component: AdminDashboardComponent, canActivate: [AdminGuard] },
  { path: 'admin/users', component: ManageUsersComponent, canActivate: [AdminGuard] },
  { path: 'admin/posts', component: ManagePostsComponent, canActivate: [AdminGuard] },
  { path: 'admin/comments', component: ManageCommentsComponent, canActivate: [AdminGuard] },
  { path: 'search-users', component: UserSearchComponent, canActivate: [AuthGuard] },
  { path: 'user-profile/:email', component: UserProfileComponent, canActivate: [AuthGuard] },
  { path: 'activity', component: ActivityComponent, canActivate: [AuthGuard] },
  { path: 'report-issue', component: ReportIssueComponent, canActivate: [AuthGuard] },
  { path: 'admin/complaints', component: AdminComplaintsComponent, canActivate: [AdminGuard] },
  { path: 'my-complaints', component: UserComplaintsComponent, canActivate: [AuthGuard] },

  { path: '**', redirectTo: '/login' }  // 👈 fallback to login
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
