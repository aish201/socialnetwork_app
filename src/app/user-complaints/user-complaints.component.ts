// src/app/user/user-complaints/user-complaints.component.ts

import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

interface Complaint {
  id: number;
  subject: string;
  category: string;
  priority: string;
  description: string;
  attachmentUrl: string;
  status: string;
  createdAt: string;
  updatedAt: string;
}

@Component({
  selector: 'app-user-complaints',
  templateUrl: './user-complaints.component.html',
  styleUrls: ['./user-complaints.component.css']
})
export class UserComplaintsComponent implements OnInit {

  complaints: Complaint[] = [];
  apiUrl = 'http://localhost:8080/api/complaints/me';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadUserComplaints();
  }

  loadUserComplaints(): void {
    const token = localStorage.getItem('token');
    if (!token) {
      console.error('No token found — please login again.');
      return;
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    this.http.get<Complaint[]>(this.apiUrl, { headers }).subscribe({
      next: (data) => {
        this.complaints = data;
        console.log('User complaints loaded:', data);
      },
      error: (err) => {
        console.error('Error fetching user complaints:', err);
      }
    });
  }
}
