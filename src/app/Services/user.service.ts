import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { User } from '../model/user';
import { UserProfileDTO } from '../model/UserProfile';

@Injectable({
  providedIn: 'root'
})
export class UserService {
 
  private baseUrl = 'http://localhost:8080/user';

  constructor(private http: HttpClient, private auth: AuthService) {}

  private getHeaders() {
    const token = this.auth.getToken();
    console.log("Token in header:", token);
    return new HttpHeaders().set('Authorization', 'Bearer ' + token);
  }

 searchUsers(keyword: string): Observable<any[]> {
  return this.http.get<any[]>(`${this.baseUrl}/search?keyword=${keyword}`, {
    headers: this.getHeaders()
  });
}

//   getUserByEmail(email: string): Observable<any> {
//   return this.http.get(`${this.baseUrl}/${email}`, {
//     headers: this.getHeaders()
//   });
// }

getProfilePicsByEmail(email: string): Observable<any> {
  return this.http.get(`${this.baseUrl}/${email}/profile-pictures`, {
    headers: this.getHeaders()
  });
}
private getAuthHeaders(): HttpHeaders {
  const token = localStorage.getItem('token');
  return new HttpHeaders({
    Authorization: `Bearer ${token}`
  });
}

getCurrentUser(): Observable<UserProfileDTO> {
  const headers = this.getAuthHeaders();
  return this.http.get<UserProfileDTO>('http://localhost:8080/user/current', { headers });
}

updateBio(bio: string): Observable<string> {
  const headers = this.getAuthHeaders();
  return this.http.put('http://localhost:8080/user/updateBio', { bio }, { headers, responseType: 'text' });
}

// Upload Profile Picture
  uploadProfilePic(formData: FormData): Observable<any> {
    console.log("upload",formData)
    const token = this.auth.getToken();
    return this.http.post(`${this.baseUrl}/upload/profile-pic`, formData, {
      headers: new HttpHeaders().set('Authorization', 'Bearer ' + token)
    });
  }
  getAllProfilePics(): Observable<any> {
  const token = this.auth.getToken();
  return this.http.get(`${this.baseUrl}/profile-pics`, {
    headers: new HttpHeaders().set('Authorization', 'Bearer ' + token)
  });
}

getBio(): Observable<any> {
  const token = this.auth.getToken();
  return this.http.get(`${this.baseUrl}/bio`, {
    headers: new HttpHeaders().set('Authorization', 'Bearer ' + token)
  });
}
// getBio(email: string): Observable<any> {
//   const token = this.auth.getToken();
//   return this.http.get(`${this.baseUrl}/${email}/bio`, {
//     headers: new HttpHeaders().set('Authorization', 'Bearer ' + token)
//   });
// }
recordSearch(email: string) {
  const token = localStorage.getItem('token');
  return this.http.post('http://localhost:8080/search/record?searchedEmail=' + email, null, {
    headers: { Authorization: 'Bearer ' + token }
  });
}
getRecentSearches() {
  const token = localStorage.getItem('token');
  return this.http.get<User[]>('http://localhost:8080/search/recent', {
    headers: { Authorization: 'Bearer ' + token }
  });
}
  getUserByEmail(email: string): Observable<UserProfileDTO> {
  return this.http.get<UserProfileDTO>(`${this.baseUrl}/${email}`);
}

}

