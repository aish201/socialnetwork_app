import { Component, OnInit, OnDestroy, NgZone } from '@angular/core';
import { Subscription } from 'rxjs';
import { NotificationService } from '../Services/notification.service';
import { AuthService } from '../Services/auth.service';
import { Notification } from '../model/notification';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-activity',
  templateUrl: './activity.component.html',
  styleUrls: ['./activity.component.css']
})
export class ActivityComponent implements OnInit, OnDestroy {
  notifications: Notification[] = [];
  isLoading = true;
  private notificationsSub?: Subscription;

  constructor(
    private notificationService: NotificationService,
    private authService: AuthService,
    private http: HttpClient,
    private router: Router,
    private ngZone: NgZone
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.isLoading = false;
      return;
    }

    this.notificationService.init();

    this.notificationsSub = this.notificationService.getNotifications()
      .subscribe((notifs) => {
        this.ngZone.run(() => {
          this.notifications = notifs;
          this.isLoading = false;
        });
      });

    this.loadInitialNotifications();
  }

  ngOnDestroy(): void {
    this.notificationsSub?.unsubscribe();
  }

  private loadInitialNotifications(): void {
    this.http.get<Notification[]>(`http://localhost:8080/api/notifications/my`)
      .subscribe({
        next: (backendNotifs) => {
          backendNotifs.forEach(n => n.displayMessage = `${n.message}`);
          const merged = [...backendNotifs, ...this.notificationService.getNotificationsSync()];
          this.notificationService.updateNotifications(merged);
          this.ngZone.run(() => this.notifications = merged);
          this.isLoading = false;
        },
        error: (err) => {
          console.error('⚠️ Failed to load initial notifications:', err);
          this.ngZone.run(() => this.isLoading = false);
        }
      });
  }

  markAllAsRead(): void {
    const updated = this.notifications.map(n => ({ ...n, readStatus: true }));
    this.notificationService.updateNotifications(updated);

    this.http.put(`http://localhost:8080/api/notifications/mark-all`, {}).subscribe();
  }

  onNotificationClick(notification: Notification): void {
    const updated = this.notifications.map(n => n === notification ? { ...n, readStatus: true } : n);
    this.notificationService.updateNotifications(updated);

    if (notification.postId) {
      this.router.navigate(['/post', notification.postId]);
    }

    this.http.put(`http://localhost:8080/api/notifications/${notification.id}/mark-read`, {}).subscribe();
  }

  trackByNotificationId(index: number, n: Notification): string {
    return `${n.id}-${n.timestamp}`;
  }

  onImgError(event: any): void {
    event.target.src = 'assets/default-avatar.png';
  }
}
