
import { TestBed } from '@angular/core/testing';
import { NotificationService } from './notification.service';
import { AuthService } from './auth.service';

class MockAuthService {
  getToken() { return 'mock-token'; }
  getUsername() { return 'mock-user'; }
}

describe('NotificationService', () => {
  let service: NotificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        NotificationService,
        { provide: AuthService, useClass: MockAuthService }
      ]
    });
    service = TestBed.inject(NotificationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
