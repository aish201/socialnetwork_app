import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Notification } from '../model/notification';
import { Client } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private stompClient: Client | null = null;
  private notifications$ = new BehaviorSubject<Notification[]>([]);

  constructor(private ngZone: NgZone) {}

  init(): void {
    if (this.stompClient && this.stompClient.active) return;

    const token = localStorage.getItem('token'); // JWT token

    this.stompClient = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      connectHeaders: { Authorization: `Bearer ${token}` },
      debug: str => console.log('STOMP:', str),
      reconnectDelay: 5000
    });

    this.stompClient.onConnect = (frame: any) => {
      console.log('✅ WebSocket STOMP connected', frame);

      this.stompClient?.subscribe('/user/queue/notifications', message => {
        const notif: Notification = JSON.parse(message.body);
        this.ngZone.run(() => {
          notif.displayMessage = `${notif.message}`;
          const current = this.notifications$.value;
          this.notifications$.next([notif, ...current]);
        });
      });
    };

    this.stompClient.activate();
  }

  getNotifications(): Observable<Notification[]> {
    return this.notifications$.asObservable();
  }

  getNotificationsSync(): Notification[] {
    return this.notifications$.value;
  }

  updateNotifications(notifs: Notification[]): void {
    this.notifications$.next(notifs);
  }

  clearNotifications(): void {
    this.notifications$.next([]);
  }
}
