import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';              // ✅ Needed for ngClass, ngIf, ngFor, date pipe
import { FormsModule } from '@angular/forms';                // ✅ Needed for [(ngModel)]
import { RouterModule } from '@angular/router';              // ✅ Needed for routerLink & router-outlet
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { JWT_OPTIONS, JwtHelperService } from '@auth0/angular-jwt';

// App Routing
import { AppRoutingModule } from './app-routing.module';

// Components
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ProfileComponent } from './profile/profile.component';
import { AdminDashboardComponent } from './admin/admin-dashboard/admin-dashboard.component';
import { ManageUsersComponent } from './admin/manage-users/manage-users.component';
import { ManagePostsComponent } from './admin/manage-posts/manage-posts.component';
import { ManageCommentsComponent } from './admin/manage-comments/manage-comments.component';
import { UserSearchComponent } from './user-search/user-search.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ActivityComponent } from './activity/activity.component';
import { ReportIssueComponent } from './report-issue/report-issue.component';
import { AdminComplaintsComponent } from './admin/admin-complaints/admin-complaints.component';

// Emoji Picker
import { EmojiModule } from '@ctrl/ngx-emoji-mart/ngx-emoji';

// Interceptors & Guards
import { TokenInterceptor } from './token.interceptor';
import { JwtChannelInterceptor } from './jwt.interceptor';
import { AuthGuard } from './auth.guard';
import { AdminGuard } from './admin.guard';
import { UserComplaintsComponent } from './user-complaints/user-complaints.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    DashboardComponent,
    ProfileComponent,
    AdminDashboardComponent,
    ManageUsersComponent,
    ManagePostsComponent,
    ManageCommentsComponent,
    UserSearchComponent,
    UserProfileComponent,
    ActivityComponent,
    ReportIssueComponent,
    AdminComplaintsComponent,
    UserComplaintsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CommonModule,      // ✅ Required for ngClass, ngIf, ngFor, date pipe
    RouterModule,      // ✅ Required for routerLink & router-outlet
    FormsModule,       // ✅ Required for [(ngModel)]
    HttpClientModule,
    EmojiModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtChannelInterceptor,
      multi: true
    },
    {
      provide: JWT_OPTIONS,
      useValue: JWT_OPTIONS
    },
    JwtHelperService,
    AuthGuard,
    AdminGuard
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent]
})
export class AppModule { }
