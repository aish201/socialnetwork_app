// import { Injectable } from '@angular/core';
// import { HttpClient, HttpHeaders } from '@angular/common/http';
// import { Observable } from 'rxjs';
// import { AuthService } from './auth.service';

// @Injectable({
//   providedIn: 'root'
// })
// export class AdminService {

//   private baseUrl = 'http://localhost:8080/admin';

//   constructor(private http: HttpClient, private auth: AuthService) {}

//   private getHeaders() {
//     const token = this.auth.getToken();
//     return new HttpHeaders().set('Authorization', 'Bearer ' + token);
//   }

//   //  Get all users
//   getAllUsers(): Observable<any> {
//     return this.http.get(`${this.baseUrl}/users`, { headers: this.getHeaders() });
//   }

//   //  Delete user
//   deleteUser(id: number): Observable<any> {
//     return this.http.delete(`${this.baseUrl}/users/${id}`, { headers: this.getHeaders() });
//   }

//   // Change user role
//   changeUserRole(id: number, role: string): Observable<any> {
//     return this.http.put(`${this.baseUrl}/users/${id}/role?role=${role}`, {}, { headers: this.getHeaders() });
//   }

//   //  Get all posts
//   getAllPosts(): Observable<any> {
//     return this.http.get(`${this.baseUrl}/posts`, { headers: this.getHeaders() });
//   }

//   //  Delete post
//   deletePost(id: number): Observable<any> {
//     return this.http.delete(`${this.baseUrl}/posts/${id}`, { headers: this.getHeaders() });
//   }

//   //  Get all comments
//   getAllComments(): Observable<any> {
//     return this.http.get(`${this.baseUrl}/comments`, { headers: this.getHeaders() });
//   }

//   // Delete comment
//   deleteComment(id: number): Observable<any> {
//     return this.http.delete(`${this.baseUrl}/comments/${id}`, { headers: this.getHeaders() });
//   }
// }
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

export interface ApiResponse {
  message: string;
}
export interface UsersResponse {
  users: any[];
}

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private baseUrl = 'http://localhost:8080/admin';

  constructor(private http: HttpClient, private auth: AuthService) {}

  private getHeaders(): HttpHeaders {
    const token = this.auth.getToken();
    return new HttpHeaders().set('Authorization', 'Bearer ' + token);
  }
  

  // Get all users
  // getAllUsers(): Observable<any> {
  //   return this.http.get(`${this.baseUrl}/users`, { headers: this.getHeaders() });
  // }
  getAllUsers(): Observable<UsersResponse> {
  return this.http.get<UsersResponse>(`${this.baseUrl}/users`, { headers: this.getHeaders() });
}

  // Delete user
  deleteUser(id: number): Observable<ApiResponse> {
    return this.http.delete<ApiResponse>(`${this.baseUrl}/users/${id}`, { headers: this.getHeaders() });
  }

  // Change user role
  changeUserRole(id: number, role: string): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.baseUrl}/users/${id}/role?role=${role}`, {}, { headers: this.getHeaders() });
  }

  // Update user status
  updateUserStatus(id: number, active: boolean): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.baseUrl}/users/${id}/status?active=${active}`, {}, { headers: this.getHeaders() });
  }

  // Get all posts
  getAllPosts(): Observable<any> {
    return this.http.get(`${this.baseUrl}/posts`, { headers: this.getHeaders() });
  }

  // Delete post
  deletePost(id: number): Observable<ApiResponse> {
    return this.http.delete<ApiResponse>(`${this.baseUrl}/posts/${id}`, { headers: this.getHeaders() });
  }

  // Get all comments
  getAllComments(): Observable<any> {
    return this.http.get(`${this.baseUrl}/comments`, { headers: this.getHeaders() });
  }

  // Delete comment
  deleteComment(id: number): Observable<ApiResponse> {
    return this.http.delete<ApiResponse>(`${this.baseUrl}/comments/${id}`, { headers: this.getHeaders() });
  }





}



