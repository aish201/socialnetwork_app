import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  getPostsWithProfiles(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/posts-with-profiles`);
  }
}
