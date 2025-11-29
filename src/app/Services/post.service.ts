import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Post } from '../model/post';
import { Comment } from '../model/comment';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private baseUrl = 'http://localhost:8080/posts';

  constructor(private http: HttpClient, private auth: AuthService) {}

  private getHeaders(): HttpHeaders {
    const token = this.auth.getToken();
    return new HttpHeaders().set('Authorization', 'Bearer ' + token);
  }

  // Load all posts with comments
 getAllPosts(): Observable<Post[]> {
  return this.http.get<Post[]>(`${this.baseUrl}`, { headers: this.getHeaders() });
}


  // Get posts by user email
  // getMyPosts(email: string): Observable<Post[]> {
  //   return this.http.get<Post[]>(`${this.baseUrl}/user/${email}`, { headers: this.getHeaders() });
  // }
  
  getMyPosts(email: string): Observable<Post[]> {
  return this.http.get<Post[]>(`http://localhost:8080/posts/myposts?email=${email}`);
}


  // Create a new post
  createPost(post: Post): Observable<any> {
    return this.http.post(this.baseUrl, post, { headers: this.getHeaders() });
  }

  // Like a post by id
  likePost(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}/like`, {}, { headers: this.getHeaders() });
  }
  
  unlikePost(id: number): Observable<any> {
  return this.http.put(`${this.baseUrl}/${id}/unlike`, {}, { headers: this.getHeaders() });
}

  // Add a comment to a post
  addComment(id: number, comment: Comment): Observable<Comment> {
    return this.http.post<Comment>(`${this.baseUrl}/${id}/comments`, comment, { headers: this.getHeaders() });
  }

  // Delete a post by id
  deletePost(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { headers: this.getHeaders() });
  }
  uploadPostImage(formData: FormData): Observable<any> {
  const token = this.auth.getToken();
  return this.http.post(`${this.baseUrl}/upload/post-image`, formData, {
    headers: new HttpHeaders().set('Authorization', 'Bearer ' + token)
  });
}

}



