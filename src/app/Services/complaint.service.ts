import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Complaint } from '../model/complaint';

@Injectable({ providedIn: 'root' })
export class ComplaintService {
  private baseUrl = 'http://localhost:8080/api/complaints';

  constructor(private http: HttpClient) {}

  private headers(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

 submitComplaint(payload: Complaint): Observable<Complaint> {
  return this.http.post<Complaint>(this.baseUrl, payload, { headers: this.headers() });
}


  uploadAttachment(file: File): Observable<{ url: string }> {
    const form = new FormData();
    form.append('file', file);
    const token = localStorage.getItem('token') || '';
    return this.http.post<{ url: string }>(`${this.baseUrl}/upload`, form, {
      headers: new HttpHeaders({ 'Authorization': `Bearer ${token}` })
    });
  }

  getMyComplaints(): Observable<Complaint[]> {
    return this.http.get<Complaint[]>(`${this.baseUrl}/me`, { headers: this.headers() });
  }

  getAllComplaints(): Observable<Complaint[]> {
    return this.http.get<Complaint[]>(this.baseUrl, { headers: this.headers() });
  }

  // updateComplaintStatus(id: number, status: 'PENDING'|'RESOLVED'|'REJECTED') {
  //   return this.http.put(`${this.baseUrl}/${id}/status`, { status }, { headers: this.headers() });
  // }
//   updateComplaintStatus(id: number, status: 'PENDING' | 'RESOLVED' | 'REJECTED') {
//   return this.http.put(`http://localhost:8080/api/complaints/${id}/status`,{ status } // request body matches backend DTO
//   );
// }
// complaint.service.ts
updateComplaintStatus(
  id: number,
  status: 'PENDING' | 'RESOLVED' | 'REJECTED'
): Observable<Complaint> {
  return this.http.put<Complaint>(`${this.baseUrl}/${id}/status`, { status });
}

}
